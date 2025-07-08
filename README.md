# EduVerify – Android Document Verification & Academic Tracker App// "Master Branch"

EduVerify is a modular and scalable Android application built to simplify academic document verification and student record management. Designed using modern Android development practices, it provides a secure, seamless experience for students and administrators alike.

## 📱 Features

- 🔐 **Authentication**  
  Secure user login and registration using Firebase Authentication.

- 📄 **Document Upload**  
  Scan or select academic documents from the gallery and store them securely via Cloudinary.

- 📊 **Academic Modules**  
  Interactive and structured modules for:
  - Attendance tracking
  - Subject management
  - Grade viewing
  - Events calendar
  - Direct messaging and announcements

- 🎨 **Modern UI**  
  Responsive layouts and intuitive navigation for smooth user experience across various screen sizes.

## 🛠️ Tech Stack

| Layer        | Technology                         |
|--------------|-------------------------------------|
| Language     | Kotlin                              |
| IDE          | Android Studio (Meerkat)            |
| SDK          | Android SDK API 34/35               |
| Backend Auth | Firebase Authentication             |
| File Storage | Cloudinary                          |
| UI           | Material Components, XML Layouts    |

## 📂 Project Structure


-EduVerify/
│
├── app/
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/com/example/eduverify/
│ │ │ │ ├── auth/ # Login/Register logic
│ │ │ │ ├── dashboard/ # Main dashboard and feature modules
│ │ │ │ ├── models/ # Data models
│ │ │ │ └── utils/ # Cloudinary, Firebase utils
│ │ │ ├── res/ # Layouts, drawables, values
│ │ │ └── AndroidManifest.xml
├── build.gradle.kts
└── README.md


## 🚀 Getting Started

### Prerequisites

- Android Studio (Meerkat or newer)
- Kotlin DSL support
- Firebase Project setup
- Cloudinary account (for media storage)

### Setup Instructions

1. **Clone the repo**
   ```bash
   git clone https://github.com/your-username/eduverify.git
   cd eduverify
🤝 Contributing
Pull requests are welcome! If you want to contribute:

Fork the repository

Create a feature branch (git checkout -b feature/your-feature)

Commit your changes (git commit -m "Add your feature")

Push to the branch (git push origin feature/your-feature)

Open a Pull Request

📃 License
This project is licensed under the MIT License - see the LICENSE file for details.

Made with ❤️ by Raj Shekhar
