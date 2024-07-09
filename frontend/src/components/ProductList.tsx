import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Button,
  Grid,
  GridItem,
  Image,
  Text,
  VStack,
} from '@chakra-ui/react';
import { currencyMarks } from '../consts';
import { sample_products } from '../consts';

const ProductList: React.FC = () => {
  const navigate = useNavigate();
  const products = sample_products;

  return (
    <Box p={5}>
      <Text fontSize="2xl" mb={5} textAlign="center">Product List</Text>
      <Grid
        templateColumns={{
          base: 'repeat(auto-fit, minmax(150px, 1fr))',
          md: 'repeat(auto-fit, minmax(200px, 1fr))',
          lg: 'repeat(auto-fit, minmax(250px, 1fr))',
        }}
        gap={6}
      >
        {products.map(product => (
          <GridItem
            key={product.id}
            bg="white"
            shadow="md"
            borderRadius="md"
            overflow="hidden"
            textAlign="center"
          >
            <VStack spacing={3} p={3} align="center">
              <Image src={product.imageUrl} alt={product.name} boxSize="100px" objectFit="cover" />
              <Text fontSize="xl" fontWeight="bold">{product.name}</Text>
              <Text fontSize="lg">{currencyMarks}{product.price}</Text>
              <Button colorScheme="teal" onClick={() => navigate(`/products/${product.id}`)}>
                View Details
              </Button>
            </VStack>
          </GridItem>
        ))}
      </Grid>
    </Box>
  );
};

export default ProductList;
