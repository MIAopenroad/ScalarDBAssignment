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
    let isSuccess = true;
    const registerOrder = async () => {
      const statements: { [key: string]: number } = {};
      cart.map((item: CartItem) => (statements[item.itemId] = item.quantity));
      console.log(statements);
      const { data } = await axios.post<boolean>("/orders/order", {
        email: userName,
        statements: statements,
      });
      isSuccess = data;
    };
    registerOrder();
    clearCart();
    navigate(isSuccess ? "/order-success" : "/order-failure");
  };

  const totalPrice = cart.reduce(
    (total, product) => total + product.price * product.quantity,
    0
  );

  return (
    <Box p={5}>
      <Text fontSize="2xl" mb={5} textAlign="center">
        Shopping Cart
      </Text>
      {cart.length === 0 ? (
        <Text textAlign="center">Your cart is empty</Text>
      ) : (
        <VStack spacing={5} align="stretch">
          {cart.map((product, index) => (
            <Box
              key={index}
              p={5}
              shadow="md"
              borderWidth="1px"
              borderRadius="md"
              overflow="hidden"
              cursor="pointer"
              onClick={() => navigate(`/products/${product.itemId}`)}
            >
              <Flex alignItems="center">
                <Image
                  src={product.imageUrl}
                  alt={product.name}
                  boxSize="100px"
                  objectFit="cover"
                  mr={5}
                />
                <Stack>
                  <Text fontSize="lg" fontWeight="bold">
                    {product.name}
                  </Text>
                  <Text>Quantity: {product.quantity}</Text>
                  <Text>
                    {currencyMarks}
                    {product.price}
                  </Text>
                </Stack>
                <IconButton
                  aria-label="Remove item"
                  icon={<FaTrash />}
                  size="sm"
                  onClick={(e) => {
                    e.stopPropagation();
                    removeFromCart(product.itemId);
                  }}
                />
              </Flex>
            </Box>
          ))}
          <Divider />
          <Box p={5} textAlign="right">
            <Text fontSize="lg" fontWeight="bold">
              Total: {currencyMarks}
              {totalPrice}
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
