import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Button,
  Image,
  Text,
  VStack,
  Flex,
  NumberInput,
  NumberInputField,
  NumberInputStepper,
  NumberIncrementStepper,
  NumberDecrementStepper,
  HStack,
} from '@chakra-ui/react';
import { useCart } from '../contexts/CartContext';
import { CartItem } from '../contexts/CartContext';
import { sample_products, currencyMarks } from '../consts';



const ProductDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const product = sample_products.find(p => p.id === parseInt(id ?? ''));  // TODO APIでDBからFind
  const { addToCart } = useCart();
  const [quantity, setQuantity] = useState(1);
  const navigate = useNavigate();

  if (!product) {
    return <Box p={5}>Product not found</Box>;
  }

  const handleAddToCart = () => {
    const addItem: CartItem = {...product, quantity: quantity};
    addToCart(addItem);
    navigate('/cart');
  };

  return (
    <Box p={5} display="flex" justifyContent="center">
      <Flex direction={{ base: 'column', md: 'row' }} align="center" justify="center" maxW="800px">
        <Box maxW="300px" mx="auto">
          <Image src={product.imageUrl} alt={product.name} boxSize="100%" objectFit="cover" />
        </Box>
        <VStack spacing={4} align="flex-start" p={{ base: 3, md: 5 }} maxW="400px">
          <Text fontSize="2xl" fontWeight="bold">{product.name}</Text>
          <Text fontSize="lg">{currencyMarks}{product.price}</Text>
          <Text>{product.description}</Text>
          <HStack>
            <NumberInput value={quantity} onChange={valueString => setQuantity(parseInt(valueString))} min={1}>
              <NumberInputField />
              <NumberInputStepper>
                <NumberIncrementStepper />
                <NumberDecrementStepper />
              </NumberInputStepper>
            </NumberInput>
            <Button colorScheme="teal" onClick={handleAddToCart}>
              Add to Cart
            </Button>
          </HStack>
        </VStack>
      </Flex>
    </Box>
  );
};

export default ProductDetail;
