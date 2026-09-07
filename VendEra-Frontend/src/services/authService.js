import api from "./api";

export async function login(email,password)
{
    const response=await api.post("/user/login",{email,password},{withCredentials: true})

    return response.data;
}

export async function logout() {
    const response = await api.get("/user/logout", {
        withCredentials: true
    })

    return response.data
}

export async function register(email, password, confirmPassword) {

    const response = await api.post("/user/register",{email,password,confirmPassword});

    return response.data;
}