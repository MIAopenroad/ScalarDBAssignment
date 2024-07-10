import ReactDOM from "react-dom/client";
import App from "./App.tsx";
import { ChakraProvider, theme } from "@chakra-ui/react";

ReactDOM.createRoot(document.getElementById("root")!).render(
    <ChakraProvider theme={theme}>
      <App />
    </ChakraProvider>
);
