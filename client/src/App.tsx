import { ConfigProvider } from "antd";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import { Footer, Header } from "./components";
import { AuthProvider } from "./contexts";
import { AboutUs } from "./pages/AboutUs";
import { CurrentlyShowingMovies } from "./pages/CurrentlyShowingMovies";
import { Home } from "./pages/Home";
import { Pricing } from "./pages/Pricing";
import { UpcomingMovies } from "./pages/UpcomingMovies";

import "./App.scss";

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <ConfigProvider
          theme={{
            components: {
              Select: {
                controlHeightLG: 48,
              },
              Drawer: {
                colorBgElevated: "#1D2939",
              },
            },
          }}
        >
          <div className="app-body">
            <Router>
              <Header />
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/about" element={<AboutUs />} />
                <Route path="/pricing" element={<Pricing />} />
                <Route
                  path="/currently-showing"
                  element={<CurrentlyShowingMovies />}
                />
                <Route path="/upcoming" element={<UpcomingMovies />} />
              </Routes>
              <Footer />
            </Router>
          </div>
        </ConfigProvider>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;
