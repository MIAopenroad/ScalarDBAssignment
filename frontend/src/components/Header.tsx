import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
import { Box, Flex, HStack, Link, Text, Button, useColorModeValue } from '@chakra-ui/react';
import { useAuth } from '../contexts/AuthContext';
import { APP_NAME } from '../consts';

const Header: React.FC = () => {
  const { isLoggedIn, userName, logout } = useAuth();
  const bg = useColorModeValue('teal.500', 'teal.300');
  const buttonBg = useColorModeValue('white', 'gray.800');
  const buttonColor = useColorModeValue('teal.500', 'teal.300');

  return (
    <Box bg={bg} px={4} py={2}>
      <Flex h={16} alignItems="center" justifyContent="space-between">
        <HStack spacing={8} alignItems="center">
          <Text fontSize="2xl" fontWeight="bold" color="white">
            {APP_NAME}
          </Text>
          <HStack as="nav" spacing={4} display={{ base: 'none', md: 'flex' }}>
            <Link as={RouterLink} to="/products" color="white">Products</Link>
            <Link as={RouterLink} to="/cart" color="white">Shopping Cart</Link>
            <Link as={RouterLink} to="/mypage" color="white">My Page</Link>
          </HStack>
        </HStack>
        {isLoggedIn && (
          <Flex alignItems="center">
            <Text color="white" mr={4}>Welcome, {userName}</Text>
            <Button
              bg={buttonBg}
              color={buttonColor}
              variant="solid"
              size="sm"
              _hover={{ bg: 'gray.200' }}
              onClick={logout}
            >
              Logout
            </Button>
          </Flex>
        )}
      </Flex>
    </Box>
  );
};

export default Header;
