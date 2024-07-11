import React, { useEffect, useState } from "react";
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
import { currencyMarks } from "../consts";
import axios from "axios";
import { OrderWithExtendedStatements } from "../types";
import { useAuth } from "../contexts/AuthContext";

const OrderHistory: React.FC = () => {
  const { userName } = useAuth();
  const [orderHistory, setOrderHistory] = useState<
    OrderWithExtendedStatements[]
  >([]);

  useEffect(() => {
    const fetchOrderHistoryByEmail = async () => {
      const { data } = await axios.get<OrderWithExtendedStatements[]>(
        `/orders/record/${userName}`
      );
      setOrderHistory(data);
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
                <Badge colorScheme={order.status ? "green" : "red"}>
                  {order.status ? "Success" : "Failure"}
                </Badge>
              </HStack>
              <Text mb={3}>Date: {order.timestamp}</Text>
              {order.extendedStatements.map((extStatement, index) => (
                <HStack key={index} spacing={5} mb={3}>
                  <Image
                    src={extStatement.item.imageUrl}
                    alt={extStatement.item.name}
                    boxSize="50px"
                    objectFit="cover"
                  />
                  <Stack>
                    <Text fontSize="md">{extStatement.item.name}</Text>
                    <Text fontSize="sm">Quantity: {extStatement.count}</Text>
                    <Text fontSize="sm">
                      {currencyMarks}
                      {extStatement.item.price}
                    </Text>
                  </Stack>
                </HStack>
              ))}
              <Divider my={3} />
              <Text fontSize="md" fontWeight="bold" textAlign="right">
                Total: {currencyMarks}
                {Math.round(order.extendedStatements.reduce((total, extStatement) => total + extStatement.item.price * extStatement.count, 0) * 100) / 100}
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
