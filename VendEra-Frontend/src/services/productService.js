import api from "./api";

export async function getProducts()
{
    const response=await api.get("/products");

    return response.data;
}
export async function addProduct(product)
{
    const response=await api.post("/admin/products", product);

    return response.data;
}