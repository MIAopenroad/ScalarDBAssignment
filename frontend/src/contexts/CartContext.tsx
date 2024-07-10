import React, { createContext, useState, ReactNode, useContext } from "react";
import { Item } from "../types";

export interface CartItem extends Item {
  quantity: number;
}

interface CartContextType {
  cart: CartItem[];
  addToCart: (product: CartItem) => void;
  removeFromCart: (productId: string) => void;
  clearCart: () => void;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [cart, setCart] = useState<CartItem[]>([]);

  const addToCart = (cartItem: CartItem) => {
    setCart((prevCart) => {
      const existingProduct = prevCart.find(
        (item) => item.itemId === cartItem.itemId
      );
      if (existingProduct) {
        return prevCart.map((item) =>
          item.itemId === cartItem.itemId
            ? { ...item, quantity: item.quantity + cartItem.quantity }
            : item
        );
      } else {
        return [...prevCart, { ...cartItem }];
      }
    });
  };
  const removeFromCart = (productId: string) => {
    setCart((prevCart) => prevCart.filter((item) => item.itemId !== productId));
  };
  const clearCart = () => setCart([]);

  return (
    <CartContext.Provider
      value={{ cart, addToCart, removeFromCart, clearCart }}
    >
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = useContext(CartContext);
  if (context === undefined) {
    throw new Error("useCart must be used within a CartProvider");
  }
  return context;
};
