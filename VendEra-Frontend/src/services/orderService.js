import api from "./api";


export async function createOrder(orderRequest)
{
    const response=await api.post("/orders",orderRequest);

    return response.data;
}


export async function getMyOrders(){
    const response=await api.get("/orders/my-orders");

    return response.data;
}


export async function  getAllOrders() {

    const response=await api.get("/admin/orders");

    return response.data
    
}