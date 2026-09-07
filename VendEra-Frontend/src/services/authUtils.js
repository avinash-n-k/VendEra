export function executeLogout() {

    // console.log("🚨 EXECUTE LOGOUT CALLED");
    localStorage.removeItem("accessToken");
    localStorage.removeItem("role");

    window.location.href = "/login";
}