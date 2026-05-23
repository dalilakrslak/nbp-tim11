import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import { ConfigProvider } from "antd";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

import { Footer, Header, SeatOptions, Payment } from "./components";
import {
  AboutUs,
  CurrentlyShowingMovies,
  Home,
  MovieDetails,
  Pricing,
  UpcomingMovies,
  ResetPassword,
} from "./pages";
import UsersAdmin from "./pages/AdminPanel/UsersAdmin";
import AdminLayout from "./pages/AdminPanel/AdminLayout";
import { AuthProvider } from "./contexts";

import UserProfile from "./pages/Profile/UserProfile";

import "./App.scss";
import MoviesAdmin from "./pages/AdminPanel/MoviesAdmin";
import VenuesAdmin from "./pages/AdminPanel/VenuesAdmin";
import ScreeningsAdmin from "./pages/AdminPanel/ScreeningsAdmin";
import Reports from "./pages/AdminPanel/Reports/Reports";

const queryClient = new QueryClient();

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <ConfigProvider
          theme={{
            components: {
              Select: {
                controlHeightLG: 48,
              },
              Carousel: {
                dotActiveWidth: 30,
                dotGap: 12,
                dotHeight: 4,
                dotOffset: 24,
                dotWidth: 30,
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
                <Route path="/profile" element={<UserProfile />} />
                <Route
                  path="/currently-showing"
                  element={<CurrentlyShowingMovies />}
                />
                <Route path="/upcoming" element={<UpcomingMovies />} />
                <Route path="/movies/:movieId" element={<MovieDetails />} />
                <Route
                  path="/screenings/:screeningId/seats"
                  element={<SeatOptions />}
                />
                <Route path="/payment/:screeningId" element={<Payment />} />
                <Route path="/reset-password" element={<div />} />

                <Route path="/admin" element={<AdminLayout />}>
                  <Route index element={<UsersAdmin />} />
                  <Route path="users" element={<UsersAdmin />} />
                  <Route path="movies" element={<MoviesAdmin />} />
                  <Route path="venues" element={<VenuesAdmin />} />
                  <Route path="screenings" element={<ScreeningsAdmin />} />
                  <Route path="reports" element={<Reports />} />
                </Route>
                <Route path="*" element={<Navigate to="/" replace />} />
              </Routes>
              <Footer />
            </Router>
          </div>
        </ConfigProvider>
      </AuthProvider>
    </QueryClientProvider>
  );
};
export default App;