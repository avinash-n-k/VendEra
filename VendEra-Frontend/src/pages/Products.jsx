import React, { useEffect, useState } from 'react'
import { getProducts } from '../services/productService'
import '../styles/Products.css'

function Products({ cart, setCart }) {

  const [products, setProducts] = useState([])
  const [quantities, setQuantities] = useState({})
  const role = localStorage.getItem("role")

  useEffect(() => {

    async function fetchProducts() {

      try {

        const response = await getProducts()

        console.log(response)

        setProducts(response)

      } catch (error) {

        console.log(error.response.data.message)

      }
    }

    fetchProducts()

  }, [])


  function addToCart(product) {

    const quantity = quantities[product.id]

    if (!quantity || quantity <= 0) {
      return
    }

    alert("Added to Cart SuccessFully")

    const cartItem = {
      productId: product.id,
      productName: product.name,
      price: product.price,
      stock: product.stock,
      quantity: quantity
    }

    const existingItem = cart.find(
      item => item.productId === product.id
    )

    if (existingItem) {

      if (existingItem.quantity + quantity > product.stock) {
        return
      }

      const updatedCart = cart.map(item => {

        if (item.productId === product.id) {

          return {
            ...item,
            quantity: item.quantity + quantity
          }

        }

        return item

      })

      setCart(updatedCart)

    } else {

      setCart([...cart, cartItem])

    }
  }


  return (

    <div className="products-page">

      <section className="products-header">

        <p className="products-eyebrow">
          VENDERA COLLECTION
        </p>

        <h1>
          Explore our <span>Products.</span>
        </h1>

        <p className="products-subtitle">
          Discover something you love and make it yours.
        </p>

      </section>


      <section className="products-grid">

        {products.map((product) => (

          <div
            className="product-card"
            key={product.id}
          >

            <div className="product-card-top">
              <span className="product-mark">✦</span>

              <span className="product-stock">
                {product.stock > 0
                  ? `${product.stock} in stock`
                  : "Out of Stock"}
              </span>
            </div>


            <div className="product-info">

              <h2>
                {product.name}
              </h2>

              <p className="product-price">
                ₹{Number(product.price).toLocaleString("en-IN")}
              </p>

            </div>


            {role === "USER" && (

              <div className="product-actions">

                {product.stock > 0 ? (

                  <>

                    <input
                      type="number"
                      min="1"
                      max={product.stock}
                      value={quantities[product.id] || ""}
                      placeholder="Qty"
                      onChange={(e) => {

                        setQuantities({
                          ...quantities,
                          [product.id]: Number(e.target.value)
                        })

                      }}
                    />

                    <button
                      onClick={() => addToCart(product)}
                    >
                      Add to Cart
                    </button>

                  </>

                ) : (

                  <p className="out-of-stock">
                    Currently unavailable
                  </p>

                )}

              </div>

            )}


            {role === "ADMIN" && (

              <div className="admin-product-info">

                <p>
                  Product ID: {product.id}
                </p>

                <p>
                  Inventory: {product.stock}
                </p>

              </div>

            )}

          </div>

        ))}

      </section>

    </div>
  )
}

export default Products