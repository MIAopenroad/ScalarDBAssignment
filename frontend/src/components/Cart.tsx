import React from "react";
import { useNavigate, Link as RouterLink } from "react-router-dom";
import {
  Box,
  Button,
  Divider,
  Flex,
  Image,
  Text,
  VStack,
  Stack,
  IconButton,
} from "@chakra-ui/react";
import { FaTrash } from "react-icons/fa";
import { CartItem, useCart } from "../contexts/CartContext";
import { currencyMarks } from "../consts";
import axios from "axios";
import { useAuth } from "../contexts/AuthContext";

const Cart: React.FC = () => {
  const { cart, removeFromCart, clearCart } = useCart();
  const navigate = useNavigate();
  const { userName } = useAuth();

  const handleOrder = () => {
    const registerOrder = async () => {
      const statements: { [key: string]: number } = {};
      cart.map((item: CartItem) => (statements[item.itemId] = item.quantity));
      const { data } = await axios.post<boolean>("/orders/order", {
        email: userName,
        statements: statements,
      });
      clearCart();
      let isSuccess = data;
      navigate(isSuccess ? "/order-success" : "/order-failure");
    };
    registerOrder();
  };

  const totalPrice = Math.round(cart.reduce(
    (total, product) => total + product.price * product.quantity, 0
  ) * 100) / 100;  // IEEE 754標準に基づく浮動小数点数の対策

  return (
    <Box p={5}>
      <Text fontSize="2xl" mb={5} textAlign="center">
        Shopping Cart
      </Text>
      {cart.length === 0 ? (
        <Text textAlign="center">Your cart is empty</Text>
      ) : (
        <VStack spacing={5} align="stretch">
          {cart.map((item, index) => (
            <Box
              key={index}
              p={5}
              shadow="md"
              borderWidth="1px"
              borderRadius="md"
              overflow="hidden"
              cursor="pointer"
              onClick={() => navigate(`/products/${item.itemId}`, { state: { item }})}
            >
              <Flex alignItems="center">
                <Image
                  src={item.imageUrl}
                  alt={item.name}
                  boxSize="100px"
                  objectFit="cover"
                  mr={5}
                />
                <Stack>
                  <Text fontSize="lg" fontWeight="bold">
                    {item.name}
                  </Text>
                  <Text>Quantity: {item.quantity}</Text>
                  <Text>
                    {currencyMarks}
                    {item.price}
                  </Text>
                </Stack>
                <IconButton
                  aria-label="Remove item"
                  icon={<FaTrash />}
                  size="sm"
                  ml={2}
                  onClick={(e) => {
                    e.stopPropagation();
                    removeFromCart(item.itemId);
                  }}
                />
              </Flex>
            </Box>
          ))}
          <Divider />
          <Box p={5} textAlign="right">
            <Text fontSize="lg" fontWeight="bold">
              Total: {currencyMarks} {totalPrice}
            </Text>
            <Button colorScheme="teal" mt={3} onClick={handleOrder}>
              Order
            </Button>
          </Box>
        </VStack>
      )}
      <Box textAlign="center" mt={5}>
        <Button colorScheme="teal" as={RouterLink} to="/products">
          Back to Products
        </Button>
      </Box>
    </Box>
  );
};

export default Cart;
