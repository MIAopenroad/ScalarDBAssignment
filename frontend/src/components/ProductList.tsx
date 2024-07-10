import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Button,
  Grid,
  GridItem,
  Image,
  Text,
  VStack,
} from "@chakra-ui/react";
import { currencyMarks } from "../consts";
import axios from "axios";
import { Item } from "../types";

const ProductList: React.FC = () => {
  const navigate = useNavigate();
  const [items, setItems] = React.useState<Item[]>([]);

  useEffect(() => {
    axios
      .get<Item[]>(`/items/all`)
      .then((response) => {
        setItems(response.data);
      })
      .catch((_) => {
        alert("Failed to fetch data from database.");
      });
  }, []);

  return (
    <Box p={5}>
      <Text fontSize="2xl" mb={5} textAlign="center">
        Product List
      </Text>
      <Grid
        templateColumns={{
          base: "repeat(auto-fit, minmax(150px, 1fr))",
          md: "repeat(auto-fit, minmax(200px, 1fr))",
          lg: "repeat(auto-fit, minmax(250px, 1fr))",
        }}
        gap={6}
      >
        {items.map((item) => (
          <GridItem
            key={item.itemId}
            bg="white"
            shadow="md"
            borderRadius="md"
            overflow="hidden"
            textAlign="center"
          >
            <VStack spacing={3} p={3} align="center">
              <Image
                src={item.imageUrl}
                alt={item.name}
                boxSize="100px"
                objectFit="cover"
              />
              <Text fontSize="xl" fontWeight="bold">
                {item.name}
              </Text>
              <Text fontSize="lg">
                {currencyMarks}
                {item.price}
              </Text>
              <Button
                colorScheme="teal"
                onClick={() =>
                  navigate(`/products/${item.itemId}`, { state: { item } })
                }
              >
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
