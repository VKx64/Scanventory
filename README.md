# 📦 InventoryAndroidApp

## 📖 Project Description

InventoryAndroidApp is an Android application designed to help users manage their inventory efficiently. The app allows users to add items, create groups, and manage orders. It also provides features for scanning QR codes, importing/exporting data, and viewing order history.

This project was developed for a client to meet their specific inventory management needs.

## ✨ Features

- Add, edit, and delete items and groups
- Scan QR codes to add items to orders
- Import and export data in Excel format
- View order history
- Manage item quantities and market-specific quantities
- Image support for items and groups

## 🛠️ Setup Instructions

### 📋 Dependencies

- Android Studio
- Java Development Kit (JDK)
- Gradle

### 🚀 Installation Steps

1. Clone the repository:
   ```sh
   git clone https://github.com/VKx64/Scanventory.git
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Build and run the project on an Android device or emulator.

## 📚 Usage Instructions

### 📝 Adding Items

1. Open the app and navigate to the desired group.
2. Tap the "Add Item" button.
3. Fill in the item details and tap "Submit".

### 🗂️ Adding Groups

1. Open the app and navigate to the desired parent group.
2. Tap the "Add Group" button.
3. Fill in the group details and tap "Submit".

### 📦 Managing Orders

1. Open the app and tap the "New Order" button.
2. Scan the QR codes of the items to add them to the order.
3. Select the desired market from the dropdown.
4. Tap "Complete Order" to finalize the order.

## 🗄️ Database Structure

The app uses Room for database management. The database consists of the following tables:

- `TableGroups`: Stores information about groups.
- `TableItems`: Stores information about items.
- `TableOrders`: Stores information about orders.
- `TableOrderItems`: Stores information about items in orders.
- `TableMarkets`: Stores market-specific quantities for items.

### 🔗 Table Relationships

- `TableGroups` has a one-to-many relationship with `TableItems`.
- `TableOrders` has a many-to-many relationship with `TableItems` through `TableOrderItems`.
- `TableItems` has a one-to-many relationship with `TableMarkets`.

## 🤝 Contributing

We welcome contributions to the project! Please follow these guidelines when submitting issues or pull requests:

1. Fork the repository and create a new branch for your feature or bugfix.
2. Write clear and concise commit messages.
3. Ensure your code follows the project's coding standards.
4. Test your changes thoroughly before submitting a pull request.
5. Provide a detailed description of your changes in the pull request.

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
