import axios from 'axios'

import { executeLogout } from './authUtils';




const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL
})

console.log("API BASE URL:", import.meta.env.VITE_API_BASE_URL)

let isRefreshing = false;
const failedQueue = [];

api.interceptors.request.use(
  (config) => {

    const publicEndpoints = [
      "/user/login",
      "/user/register",
      "/user/refresh",
      "/user/logout"
    ]

    if (publicEndpoints.includes(config.url) == false) {

      const token = localStorage.getItem("accessToken")

      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
    }

    return config
  },

  (error) => {
    return Promise.reject(error)
  }
)


api.interceptors.response.use(

  (response) => {
    return response;
  },

  async (error) => {


    // console.log("🔥 RESPONSE ERROR INTERCEPTOR");
    // console.log("URL:", error.config?.url);
    // console.log("STATUS:", error.response?.status);
    // console.log("DATA:", error.response?.data);

    const originalRequest = error.config;
    const status = error.response?.status;
    const message = error.response?.data?.message;

    if (status === 401 && message==="JWT Token Expired" && originalRequest.url.includes("/login") === false &&
    originalRequest.url.includes("/register")===false &&
    originalRequest.url.includes("/refresh")===false) {

      if (isRefreshing) {

        return new Promise((resolve, reject) => {

          failedQueue.push({ resolve, reject });

        }).then((token) => {

          originalRequest.headers.Authorization = `Bearer ${token}`;

          return api(originalRequest);

        });

      }

      else {

        isRefreshing = true;

        try {

          const response = await api.post("/user/refresh",
            {},
            {
              withCredentials: true,
            }
          );

          const token = response.data.data.accessToken;

          localStorage.setItem("accessToken", token);

          failedQueue.forEach((queueItem) => {
            queueItem.resolve(token);
          });

          failedQueue.length = 0;

          originalRequest.headers.Authorization = `Bearer ${token}`;

          return api(originalRequest);

        }

        catch (error) {

          failedQueue.forEach((queueItem) => {
            queueItem.reject(error);
          });

          failedQueue.length = 0;

          executeLogout();

          return Promise.reject(error);

        }

        finally {

          isRefreshing = false;

        }
      }
    }
    else if(status === 401)
    {
      executeLogout();

      return Promise.reject(error);
    }
    else
    {
         return Promise.reject(error);
    }
   

  }

);





export default api