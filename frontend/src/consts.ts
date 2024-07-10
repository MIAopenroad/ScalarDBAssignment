export const APP_NAME = "Arakimura's EC Shop";
export const API_URL = 'http://localhost:3001';
export const currencyMarks = '¥';
export const sample_products = [
    { id: 1, name: 'Product 1', price: 100, description: 'Description for Product 1', imageUrl: 'https://via.placeholder.com/300' },
    { id: 2, name: 'Product 2', price: 200, description: 'Description for Product 2', imageUrl: 'https://via.placeholder.com/300' },
    { id: 3, name: 'Product 3', price: 300, description: 'Description for Product 3', imageUrl: 'https://via.placeholder.com/300' },
    { id: 4, name: 'Product 4', price: 400, description: 'Description for Product 4', imageUrl: 'https://via.placeholder.com/300' },
    { id: 5, name: 'Product 5', price: 500, description: 'Description for Product 5', imageUrl: 'https://via.placeholder.com/300' },
    { id: 6, name: 'Product 6', price: 600, description: 'Description for Product 6', imageUrl: 'https://via.placeholder.com/300' },
    { id: 7, name: 'Product 7', price: 700, description: 'Description for Product 7', imageUrl: 'https://via.placeholder.com/300' },
    { id: 8, name: 'Product 8', price: 800, description: 'Description for Product 8', imageUrl: 'https://via.placeholder.com/300' },
    { id: 9, name: 'Product 9', price: 900, description: 'Description for Product 9', imageUrl: 'https://via.placeholder.com/300' },
    { id: 10, name: 'Product 10', price: 1000, description: 'Description for Product 10', imageUrl: 'https://via.placeholder.com/300' },
];
export const sample_orderHistory = [
    {
      orderId: 1,
      products: [
        { id: 1, name: 'Product 1', price: 100, quantity: 2, imageUrl: 'https://via.placeholder.com/150' },
        { id: 2, name: 'Product 2', price: 200, quantity: 1, imageUrl: 'https://via.placeholder.com/150' },
      ],
      total: 400,
      date: '2023-07-01',
      status: 'success',
    },
    {
      orderId: 2,
      products: [
        { id: 3, name: 'Product 3', price: 150, quantity: 4, imageUrl: 'https://via.placeholder.com/150' },
        { id: 4, name: 'Product 4', price: 250, quantity: 2, imageUrl: 'https://via.placeholder.com/150' },
      ],
      total: 1100,
      date: '2023-07-02',
      status: 'failure',
    },
  ];