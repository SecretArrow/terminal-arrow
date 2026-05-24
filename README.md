# Terminal Arrow - Ultimate Terminal & Network Utility

Terminal Arrow is a premium, professional-grade terminal emulator and SSH/SFTP client for Android, meticulously crafted with Kotlin and Jetpack Compose. Designed for system administrators, developers, and power users, it provides a desktop-class experience in the palm of your hand.

**Developed by: Maragung**

---

## 🚀 Key Features

### 💻 Advanced Terminal & Connectivity
*   **Multi-Protocol Support:** Full support for SSH, SFTP, and Local Shell.
*   **Bitvise-Grade Tunneling:** Powerful Port Forwarding capabilities including Local, Remote, and Dynamic (SOCKS) tunnels.
*   **Wake Lock & Background Persistence:** Keep your sessions alive even when the app is in the background or the screen is off via a dedicated Foreground Service.
*   **Terminal Bell & Haptics:** Tactile feedback for terminal events (ASCII Bell).
*   **Split View:** Run two terminal sessions simultaneously in a vertical or horizontal split.

### 📁 SFTP File Manager & Built-in Editor
*   **Visual File Browser:** Navigate remote file systems with ease.
*   **Integrated Code Editor:** Edit remote files directly (e.g., config files, code) with a built-in text editor and auto-upload on save.
*   **Seamless Transfers:** Upload and download files between your Android device and remote servers.

### 🛡️ Security & Key Management
*   **Biometric Lock:** Secure your data with Fingerprint or Face ID authentication on startup.
*   **Local Encrypted Vault:** Your server credentials and snippets are stored securely in a local Room database.
*   **Modern Cryptography:** Manage SSH keys with support for RSA-2048 and Ed25519 algorithms.
*   **Agent Forwarding:** Securely use your local keys on remote hosts.

### 🎨 Premium UI/UX & Customization
*   **Modern Material 3 Design:** A clean, intuitive, and responsive interface.
*   **Dynamic Theming:** Choose from curated themes like Matrix, Dracula, Ocean, and Classic.
*   **Font Customization:** Adjustable font sizes (8sp - 32sp) and multiple font families (Monospace, Serif, etc.).
*   **Search & Highlighting:** Easily search through terminal buffers with visual text highlighting.
*   **Smart Autocomplete:** Intelligent command suggestions to speed up mobile typing.
*   **Profile Grouping:** Organize your servers into folders and categories.

### ☁️ Integration & Maintenance
*   **Cloud Import:** Automatically sync instance lists from AWS (EC2) and DigitalOcean (Droplets).
*   **Backup & Restore:** Export all your account profiles to a JSON file and restore them anytime.
*   **Snippets Manager:** Save and execute frequently used commands with one click.
*   **Screen Export:** Export terminal session logs directly to .txt files for documentation.

---

## 🛠 Technical Specifications

### Architecture
*   **Language:** 100% Kotlin.
*   **UI Framework:** Jetpack Compose (Declarative UI).
*   **Dependency Injection:** Hilt (Dagger) for robust and testable code.
*   **Navigation:** Navigation Compose for seamless multi-screen transitions.
*   **Database:** Room Persistence Library for local vault management.

### Libraries & Engines
*   **SSH/SFTP Engine:** `SSHJ` - A modern, high-performance SSHv2 library.
*   **Cryptography:** `BouncyCastle` for advanced security algorithms.
*   **Cloud SDKs:** AWS Mobile SDK for Android, OkHttp for DigitalOcean API.
*   **Utility:** Gson for JSON serialization, AndroidX Biometrics for security.

### Build Configuration
*   **Min SDK:** 26 (Android 8.0).
*   **Target SDK:** 34 (Android 14).
*   **ABI Split:** Optimized builds for `arm64-v8a`, `armeabi-v7a`, `x86`, and `x86_64`.
*   **ProGuard/R8:** Fully configured for code shrinking and obfuscation without breaking SSHJ/SFTP functionality.

---

## 📦 How to Build

1.  Clone this repository to your local machine.
2.  Open the project in **Android Studio (Hedgehog or newer)**.
3.  Ensure you have **JDK 17** configured.
4.  Run `./gradlew assembleRelease` to generate the signed APKs.

---

*Terminal Arrow - Excellence in Mobile Networking.*
