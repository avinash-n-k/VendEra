import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { addProduct } from '../services/productService'
import '../styles/AddProduct.css'

function AddProduct() {

  const navigate = useNavigate()

  const [name, setName] = useState("")
  const [price, setPrice] = useState("")
  const [stock, setStock] = useState("")

  async function handleSubmit(e) {

    e.preventDefault()

    const product = {
      name: name,
      price: price,
      stock: stock
    }

    console.log(product)

    try {

      const response = await addProduct(product)

      console.log("Product Added:", response)

      alert("Product Added Successfully")

      setName("")
      setPrice("")
      setStock("")

    } catch (error) {

      console.log(
        "Failed to add product:",
        error
      )

    }
  }


  return (

    <div className="add-product-page">

      <div className="add-product-container">

        <button
          className="back-button"
          onClick={() => navigate("/home")}
        >
          ← Back to Home
        </button>


        <section className="add-product-header">

          <p className="add-product-eyebrow">
            VENDERA ADMIN
          </p>

          <h1>
            Add <span>Product.</span>
          </h1>

          <p>
            Add a new product to the VendEra inventory.
          </p>

        </section>


        <form
          className="add-product-form"
          onSubmit={handleSubmit}
        >

          <div className="form-group">

            <label>
              Product Name
            </label>

            <input
              type="text"
              placeholder="Enter product name"
              value={name}
              onChange={(e) =>
                setName(e.target.value)
              }
              required
            />

          </div>


          <div className="form-row">

            <div className="form-group">

              <label>
                Price
              </label>

              <div className="input-with-symbol">

                <span>
                  ₹
                </span>

                <input
                  type="number"
                  placeholder="0"
                  min="0"
                  value={price}
                  onChange={(e) =>
                    setPrice(e.target.value)
                  }
                  required
                />

              </div>

            </div>


            <div className="form-group">

              <label>
                Stock
              </label>

              <input
                type="number"
                placeholder="0"
                min="0"
                value={stock}
                onChange={(e) =>
                  setStock(e.target.value)
                }
                required
              />

            </div>

          </div>


          <button
            className="add-product-button"
            type="submit"
          >
            Add Product
          </button>

        </form>

      </div>

    </div>
  )
}

export default AddProduct