# 📱 mPhone

**mPhone** is a modern, lightweight Android SIP/VoIP softphone client built with **Kotlin**, **Jetpack Compose**, and the **Linphone SDK**. Designed for reliability and clarity, it supports multi-account SIP registration, encrypted local credential storage, and real-time call state management.

---

## ✨ Key Features

- **Multi-Account SIP Support:** Register and manage multiple SIP accounts simultaneously with live registration status tracking (`Active`, `Registering...`, `Failed`).
- **Modern Jetpack Compose UI:** Clean Material 3 interface with intuitive dialer, account switcher, and live incoming/outgoing call screens.
- **Powered by Linphone SDK:** High-reliability VoIP audio engine with full SIP signaling and call state callbacks.
- **Encrypted Credential Storage:** Sensitive SIP credentials and account secrets are protected using **Android Keystore** encryption.
- **Local Call & Account Persistence:** Fast, offline-ready storage powered by **Room Database** and Kotlin Coroutines/Flow.
- **Automated Cloud Builds:** Pre-configured **GitHub Actions** workflow for one-click remote debug APK compilation.

---

## 🛠️ Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose (Material 3) |
| **VoIP / SIP Engine** | Linphone SDK |
| **Local Database** | Room Persistence Library (KSP) |
| **Security** | Android Keystore System |
| **Async / State** | Kotlin Coroutines & StateFlow |
| **Build System** | Gradle 8.9 (JDK 17) |

---

## 🚀 Building from Source

### Prerequisites
- **JDK 17**
- **Android SDK** (Command-Line Tools / Build-Tools)
- **Gradle 8.9+**

### Build Debug APK (CLI / Codespaces)

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/mamavhuk/mPhone.git](https://github.com/mamavhuk/mPhone.git)
   cd mPhone
