import React, { createContext, useState, ReactNode, useContext } from 'react';

interface AuthContextType {
  isLoggedIn: boolean;
  userName: string | null;
  login: (loginUser: string) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userName, setUserName] = useState<string | null>(null);

  const login = (loginUser: string) => {
    setIsLoggedIn(true);
    setUserName(loginUser);
  }
  const logout = () => {
    setIsLoggedIn(false);
    setUserName(null);
  }

  return (
    <AuthContext.Provider value={{ isLoggedIn, userName, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
