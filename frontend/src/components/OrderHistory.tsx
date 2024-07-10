import React, { useEffect } from "react";
import { Link as RouterLink } from "react-router-dom";
import {
  Box,
  Button,
  Text,
  VStack,
  HStack,
  Image,
  Stack,
  Divider,
  Badge,
} from "@chakra-ui/react";
import { sample_orderHistory, currencyMarks } from "../consts";
import axios from "axios";
import { OrderWithStatements } from "../types";
import { useAuth } from "../contexts/AuthContext";

const OrderHistory: React.FC = () => {
  const { userName } = useAuth();
  const orderHistory = sample_orderHistory;

  useEffect(() => {
    const fetchOrderHistoryByEmail = async () => {
      const { data } = await axios.get<OrderWithStatements>(
        `/orders/record/${userName}`
      );
      console.log("orderHistoryByEmail>>", data);
    };
    fetchOrderHistoryByEmail();
  }, []);

  return (
    <Box p={5}>
      <Text fontSize="2xl" mb={5} textAlign="center">
        My Page
      </Text>
      {orderHistory.length === 0 ? (
        <Text textAlign="center">You have no order history.</Text>
      ) : (
        <VStack spacing={5} align="stretch">
          {orderHistory.map((order) => (
            <Box
              key={order.orderId}
              p={5}
              shadow="md"
              borderWidth="1px"
              borderRadius="md"
              overflow="hidden"
            >
              <HStack justify="space-between">
                <Text fontSize="lg" fontWeight="bold">
                  Order #{order.orderId}
                </Text>
                <Badge
                  colorScheme={order.status === "success" ? "green" : "red"}
                >
                  {order.status === "success" ? "Success" : "Failure"}
                </Badge>
              </HStack>
              <Text mb={3}>Date: {order.date}</Text>
              {order.products.map((product, index) => (
                <HStack key={index} spacing={5} mb={3}>
                  <Image
                    src={product.imageUrl}
                    alt={product.name}
                    boxSize="50px"
                    objectFit="cover"
                  />
                  <Stack>
                    <Text fontSize="md">{product.name}</Text>
                    <Text fontSize="sm">Quantity: {product.quantity}</Text>
                    <Text fontSize="sm">
                      {currencyMarks}
                      {product.price}
                    </Text>
                  </Stack>
                </HStack>
              ))}
              <Divider my={3} />
              <Text fontSize="md" fontWeight="bold" textAlign="right">
                Total: {currencyMarks}
                {order.total}
              </Text>
            </Box>
          ))}
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

export default OrderHistory;
