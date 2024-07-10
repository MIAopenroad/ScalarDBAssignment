import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  Box,
  Button,
  FormControl,
  FormLabel,
  Input,
  Text,
  VStack,
} from '@chakra-ui/react';
import axios from 'axios';
import { useAuth } from '../contexts/AuthContext';
import { useCart } from '../contexts/CartContext';


const SignUp: React.FC = () => {
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState("");
  const [confirmPassword, setConfirmPassword] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();
  const { clearCart } = useCart();
  const location = useLocation();

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    axios.post("/customers/signup", {
        email: userName,
        password: password,
    })
    .then((response) => {
        if (response.data) {
        login(userName);
        clearCart();
        if (location.state) {
            navigate(location.state, { replace: true })
        } else {
            navigate(`/products`, { replace: true })
        }
        } else {
        console.error(response.statusText);
        setError('既に登録済みのユーザーです。');
        }
    })
    .catch((error) => {
        console.error(error);
        setError('通信に失敗しました。');
    });
  };

  return (
    <>
      <Box p={5} maxW="500px" mx="auto">
        <Text fontSize="2xl" mb={5} textAlign="center">Sign Up</Text>
        <form onSubmit={handleSubmit}>
          <VStack spacing={4} align="stretch">
            <FormControl id="userName" isRequired>
              <FormLabel>UserName</FormLabel>
              <Input type="text" value={userName} onChange={(e) => setUserName(e.target.value)} />
            </FormControl>
            <FormControl id="password" isRequired>
              <FormLabel>Password</FormLabel>
              <Input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
            </FormControl>
            <FormControl id="confirm-password" isRequired>
              <FormLabel>Confirm Password</FormLabel>
              <Input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
            </FormControl>
            {error && <div style={{ color: 'red' }}>{error}</div>}
            <Button colorScheme="teal" type="submit">Sign Up</Button>
          </VStack>
        </form>
      </Box>
    </>
  );
};

export default SignUp;
