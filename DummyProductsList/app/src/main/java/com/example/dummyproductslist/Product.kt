package com.example.dummyproductslist

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val iconResId: Int
)

val dummyProducts = listOf(
    Product(1, "Product 1", "This is the first product with some details.", R.drawable.ic_productone),
    Product(2, "Product 2", "This is the second product with more details.", R.drawable.ic_producttwo),
    Product(3, "Product 3", "This is the third product with even more details.", R.drawable.ic_productthree),
    Product(4, "Product 4", "This is the third product with even more details.", R.drawable.ic_productthree),
    Product(5, "Product 5", "This is the third product with even more details.", R.drawable.ic_productone),
    Product(6, "Product 6", "This is the third product with even more details.", R.drawable.ic_producttwo),
    Product(7, "Product 7", "This is the third product with even more details.", R.drawable.ic_productthree),
    Product(8, "Product 8", "This is the third product with even more details.", R.drawable.ic_productone),
    Product(9, "Product 9", "This is the third product with even more details.", R.drawable.ic_producttwo),
    Product(10, "Product 10", "This is the third product with even more details.", R.drawable.ic_productthree),
    Product(11, "Product 11", "This is the third product with even more details.", R.drawable.ic_productone),
    Product(12, "Product 12", "This is the third product with even more details.", R.drawable.ic_producttwo),
    Product(13, "Product 14", "This is the third product with even more details.", R.drawable.ic_productthree)




)