import React from 'react';
import { Box, Button, Text, VStack } from '@chakra-ui/react';
import { Link as RouterLink } from 'react-router-dom';

const OrderSuccess: React.FC = () => {
  return (
    <Box p={5} textAlign="center">
      <VStack spacing={5}>
        <Text fontSize="2xl" fontWeight="bold">Order Successful!</Text>
        <Text>Your order has been placed successfully.</Text>
        <Button colorScheme="teal" as={RouterLink} to="/products">
          Back to Products
        </Button>
      </VStack>
    </Box>
  );
};

export default OrderSuccess;
