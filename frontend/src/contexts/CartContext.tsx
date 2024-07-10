import React, { createContext, useState, ReactNode, useContext } from 'react';

export interface Product {
  id: number;
  name: string;
  price: number;
  imageUrl: string;
}

export interface CartItem extends Product {
  quantity: number;
}

interface CartContextType {
  cart: CartItem[];
  addToCart: (product: CartItem) => void;
  removeFromCart: (productId: number) => void;
  clearCart: () => void;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [cart, setCart] = useState<CartItem[]>([]);

  const addToCart = (cartItem: CartItem) => {
    setCart(prevCart => {
      const existingProduct = prevCart.find(item => item.id === cartItem.id);
      if (existingProduct) {
        return prevCart.map(item =>
          item.id === cartItem.id ? { ...item, quantity: item.quantity + cartItem.quantity } : item
        );
      } else {
        return [...prevCart, { ...cartItem}];
      }
    });
  };
  const removeFromCart = (productId: number) => {
    setCart(prevCart => prevCart.filter(item => item.id !== productId));
  };
  const clearCart = () => setCart([]);

  return (
    <CartContext.Provider value={{ cart, addToCart, removeFromCart, clearCart }}>
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = useContext(CartContext);
  if (context === undefined) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};
