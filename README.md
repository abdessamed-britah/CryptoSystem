# 🔐 CryptoSyst

<div align="center">

### A Professional Desktop Encryption & Decryption System

**Built with Java Swing — Featuring AES-128, DES-56 & RSA-2048**

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-Desktop_GUI-007396?style=for-the-badge&logo=java&logoColor=white)
![JCE](https://img.shields.io/badge/JCE-Cryptography-4CAF50?style=for-the-badge&logo=letsencrypt&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)
![Version](https://img.shields.io/badge/Version-2.0-purple?style=for-the-badge)

---

*Encrypt, decrypt and explore three fundamental cryptographic algorithms*
*through a modern dark-themed desktop interface.*

</div>

---

## 📑 Table of Contents

1. [Overview](#-overview)
2. [Learning Objectives](#-learning-objectives)
3. [Features](#-features)
4. [Supported Algorithms](#-supported-algorithms)
   - [AES-128 (CBC)](#-aes-128--advanced-encryption-standard)
   - [DES-56 (CBC)](#-des-56--data-encryption-standard)
   - [RSA-2048](#-rsa-2048--rivest-shamir-adleman)
   - [Symmetric vs Asymmetric](#symmetric-vs-asymmetric--side-by-side)
5. [Data Flow](#-data-flow)
6. [Application Architecture](#-application-architecture)
7. [Getting Started](#-getting-started)
8. [User Guide](#-user-guide)
9. [Keyboard Shortcuts](#-keyboard-shortcuts)
10. [Technical Deep Dive](#-technical-deep-dive)
11. [Security](#-security)
12. [Known Limitations](#-known-limitations)
13. [Technologies Used](#-technologies-used)
14. [Project Structure](#-project-structure)

---

## 🎯 Overview

**CryptoSyst** is a cryptography mini-project developed as part of an Information Security course. It is a single-file Java Swing desktop application that lets users:

- **Encrypt** plaintext into unreadable ciphertext using industry-standard algorithms
- **Decrypt** ciphertext back to the original message
- **Experiment** hands-on with three foundational cryptographic algorithms
- **Visualize** the relationship between keys, initialization vectors, and ciphertext

The application ships with a sleek **dark theme** (with light mode toggle), **gradient header**, **rounded card panels**, **pill-shaped action buttons**, and a **color-coded status bar** — providing a polished, professional user experience.

---

## 🎓 Learning Objectives

This project is designed to teach and demonstrate the following concepts:

| # | Concept | What You'll Learn |
|:-:|---------|-------------------|
| 1 | **Symmetric Encryption** | How a single shared key encrypts AND decrypts data (AES, DES) |
| 2 | **Asymmetric Encryption** | How a public/private key pair works — one encrypts, the other decrypts (RSA) |
| 3 | **Block Cipher Modes** | Why **CBC** (Cipher Block Chaining) is secure and **ECB** is not |
| 4 | **Initialization Vectors (IV)** | How a random IV makes every encryption output unique — even with the same key and plaintext |
| 5 | **Base64 Encoding** | How raw binary bytes are represented as printable ASCII text |
| 6 | **Key Management** | Secure generation, storage, and lifecycle of cryptographic keys |
| 7 | **Padding (PKCS5)** | How plaintext is padded to fill the last block in a block cipher |
| 8 | **Java Swing GUI** | Building a professional desktop interface with custom-painted components |

---

## ✨ Features

### Core Functionality

| Feature | Description |
|---------|-------------|
| 🔒 **Multi-Algorithm Encryption** | Choose between AES-128, DES-56, or RSA-2048 from a dropdown |
| 🔓 **Decryption** | Recover the original plaintext from any ciphertext produced by this app |
| ⚡ **One-Click Key Generation** | Generate cryptographically secure keys instantly using `SecureRandom` |
| 📂 **File Import** | Load text from any local file directly into the input area |
| 💾 **Result Export** | Save encrypted or decrypted output to a file |
| 📋 **Clipboard Copy** | Copy the result to the system clipboard with a single click |
| 🗑️ **Secure Clear** | Wipe all fields and destroy RSA keys from memory with confirmation |

### User Interface

| Feature | Description |
|---------|-------------|
| 🌗 **Dark / Light Theme Toggle** | Switch themes instantly via the ☾/☀ button in the header |
| 🎨 **Gradient Header** | Indigo-to-purple gradient with app title and algorithm tagline |
| 🃏 **Rounded Card Panels** | All sections rendered as cards with 14px rounded corners and subtle borders |
| 💊 **Pill-Shaped Action Buttons** | Encrypt, Decrypt, and Clear buttons with drop shadows and hover effects |
| 📊 **Live Character Counter** | Real-time character count for both input and output areas |
| 🔑 **Key Size Indicator** | Shows key strength in bits — green (≥128-bit), amber (weaker), red (invalid) |
| 📝 **Color-Coded Status Bar** | Green for success, amber for warnings, red for errors — auto-resets after 2.5s |
| ⌨️ **Keyboard Shortcuts** | Ctrl+E (encrypt), Ctrl+D (decrypt), Ctrl+G (generate key) |

---

## 🔬 Supported Algorithms

### 🟢 AES-128 — Advanced Encryption Standard

| Property | Value |
|----------|-------|
| **Type** | Symmetric block cipher |
| **Key Size** | 128 bits (16 bytes) |
| **Block Size** | 128 bits (16 bytes) |
| **Mode** | CBC (Cipher Block Chaining) |
| **Padding** | PKCS5 |
| **IV Size** | 128 bits (randomly generated per encryption) |

**AES** is the gold standard of symmetric encryption, adopted by NIST in 2001 to replace DES. It is used globally in TLS/SSL, VPNs, disk encryption, and virtually every modern security system.

#### How AES-CBC Works in This App

```
                        ┌──────────┐
                        │  Secret  │
                        │   Key    │
                        │ (16 B)   │
                        └────┬─────┘
                             │
 Plaintext                   │                              Ciphertext
┌────────┐              ┌────▼─────┐                      ┌──────────┐
│Block 1 │──XOR──┐      │          │                      │          │
└────────┘       │      │   AES    │                      │    IV    │  ← 16 random bytes
┌────────┐   ┌───▼───┐  │  Engine  │──────────────────┐   │──────────│
│  IV    │──►│ XOR'd │─►│          │──► Cipherblock 1 ├──►│  Block 1 │
└────────┘   └───────┘  └────┬─────┘                  │   │──────────│
                             │                         │   │  Block 2 │
┌────────┐              ┌────▼─────┐                  │   │──────────│
│Block 2 │──XOR──┐      │          │                  │   │   ...    │
└────────┘       │      │   AES    │                  │   │──────────│
    CB1 ────────►│─────►│  Engine  │──► Cipherblock 2 │   │  Block N │
                 │      │          │                  │   └──────────┘
                 └──────└──────────┘──────────────────┘
                                                            Base64
                    Each block XORs with the                encoded
                    previous cipherblock (CBC)              output
```

**Encryption steps:**
1. Decode the Base64 key → raw 16 bytes
2. Generate a random 16-byte IV using `SecureRandom`
3. Convert plaintext to bytes using UTF-8
4. Encrypt using AES/CBC/PKCS5Padding with the key and IV
5. Prepend the IV to the ciphertext: `output = IV || ciphertext`
6. Encode the combined bytes as Base64

**Decryption steps:**
1. Decode the Base64 key → raw 16 bytes
2. Decode the Base64 message → raw bytes
3. Split: first 16 bytes = IV, remainder = ciphertext
4. Decrypt using the same key and extracted IV
5. Convert the result bytes to a UTF-8 string

#### Why CBC Instead of ECB?

| | ECB (Electronic Codebook) ❌ | CBC (Cipher Block Chaining) ✅ |
|---|---|---|
| **How it works** | Each block encrypted independently | Each block XOR'd with previous cipherblock before encryption |
| **Same input blocks** | Produce identical output blocks | Produce completely different output blocks |
| **Pattern leakage** | Reveals data patterns (famously visible in images) | No patterns — output appears fully random |
| **IV required?** | No | Yes — random IV ensures uniqueness |
| **Security** | **Broken** for structured data | **Industry standard** — secure for all data types |

> 💡 **Tip:** Search "ECB penguin" online to see a famous visual demonstration of why ECB mode leaks patterns.

---

### 🟡 DES-56 — Data Encryption Standard

| Property | Value |
|----------|-------|
| **Type** | Symmetric block cipher |
| **Key Size** | 56 bits effective (64 bits with 8 parity bits) |
| **Block Size** | 64 bits (8 bytes) |
| **Mode** | CBC (Cipher Block Chaining) |
| **Padding** | PKCS5 |
| **IV Size** | 64 bits (8 bytes, randomly generated) |

**DES** was the dominant encryption standard from 1977 to the late 1990s. It was broken by brute-force attack in 1999 (the EFF's "Deep Crack" machine found a DES key in 22 hours). It is included here **for educational purposes only**.

The implementation in this app is identical to AES-CBC, with smaller key (8 bytes) and block (8 bytes) sizes.

> ⚠️ **Warning:** DES is cryptographically broken and must **never** be used to protect real data. Use AES-128 or stronger.

---

### 🔵 RSA-2048 — Rivest-Shamir-Adleman

| Property | Value |
|----------|-------|
| **Type** | Asymmetric (public-key) cipher |
| **Key Size** | 2048 bits |
| **Padding** | PKCS1 v1.5 |
| **Max Plaintext** | ~245 bytes (2048/8 - 11 bytes overhead) |
| **Based On** | The computational difficulty of factoring large semiprimes |

**RSA** is the most widely known asymmetric encryption algorithm. Unlike AES/DES, it uses **two mathematically linked keys**:

```
  ┌─────────────────────────────────────────────────────────┐
  │                    RSA KEY PAIR                         │
  │                                                         │
  │   ┌──────────────┐          ┌───────────────┐           │
  │   │  PUBLIC KEY   │          │  PRIVATE KEY  │           │
  │   │  (shareable)  │          │   (secret)    │           │
  │   └──────┬───────┘          └───────┬───────┘           │
  │          │                          │                   │
  │          ▼                          ▼                   │
  │   Used to ENCRYPT            Used to DECRYPT            │
  │   Anyone can encrypt         Only the owner can read    │
  └─────────────────────────────────────────────────────────┘

  ENCRYPTION:
  ┌───────────┐    ┌────────────┐    ┌───────────────┐
  │ Plaintext │───►│ Public Key │───►│  Ciphertext   │
  └───────────┘    └────────────┘    └───────┬───────┘
                                             │
  DECRYPTION:                                │
  ┌───────────────┐    ┌─────────────┐    ┌──▼────────┐
  │   Plaintext   │◄───│ Private Key │◄───│ Ciphertext│
  └───────────────┘    └─────────────┘    └───────────┘
```

**How RSA works in this app:**
1. A 2048-bit key pair is generated automatically on first encryption
2. The **public key** (truncated) is displayed in the key field for reference
3. The **private key** is kept in memory — it is never displayed or exported
4. Decryption uses the in-memory private key
5. Clearing all fields destroys the key pair

---

### Symmetric vs Asymmetric — Side by Side

| Criteria | AES / DES (Symmetric) | RSA (Asymmetric) |
|----------|----------------------|-------------------|
| **Keys** | 1 shared secret key | 2 keys (public + private) |
| **Speed** | ⚡ Very fast (hardware-accelerated) | 🐢 Slow (heavy math) |
| **Data size limit** | Unlimited (block-by-block) | ~245 bytes for RSA-2048 |
| **Key distribution** | Hard — how to share the secret securely? | Easy — public key is public |
| **Typical use** | Bulk data encryption | Key exchange, digital signatures |
| **Real-world combo** | TLS uses RSA to exchange an AES key, then AES encrypts the data | ← Same |

---

## 🔄 Data Flow

### Encryption Pipeline

```
┌───────────┐     ┌───────────┐     ┌─────────────────┐     ┌───────────┐
│           │     │           │     │                 │     │           │
│ Plaintext │────►│  UTF-8    │────►│   Algorithm     │────►│  Base64   │
│  (user    │     │  encode   │     │  (AES/DES/RSA)  │     │  encode   │──── Output
│   input)  │     │           │     │   + Key + IV    │     │           │
│           │     │           │     │                 │     │           │
└───────────┘     └───────────┘     └─────────────────┘     └───────────┘
                                           ▲    ▲
                                           │    │
                                     ┌─────┘    └─────┐
                                     │                │
                                ┌────┴────┐     ┌─────┴────┐
                                │   Key   │     │ Random   │
                                │ (Base64)│     │   IV     │
                                └─────────┘     └──────────┘
```

### Decryption Pipeline

```
┌───────────┐     ┌───────────┐     ┌──────────┐     ┌──────────────┐     ┌──────────┐
│           │     │           │     │          │     │              │     │          │
│  Base64   │────►│  Decode   │────►│ Split IV │────►│  Algorithm   │────►│  UTF-8   │── Output
│  input    │     │  to bytes │     │  + data  │     │  decrypt     │     │  decode  │
│           │     │           │     │          │     │  (Key + IV)  │     │          │
└───────────┘     └───────────┘     └──────────┘     └──────────────┘     └──────────┘
```

---

## 🏗️ Application Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CryptoSyst.java                            │
│                        (Single-File App)                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ╔═══════════════════════════════════════════════════════════════╗  │
│  ║                     CONSTANTS LAYER                          ║  │
│  ║  • Dark theme palette (7 colors)                             ║  │
│  ║  • Light theme palette (7 colors)                            ║  │
│  ║  • Accent colors (6: primary, success, info, warning, etc.)  ║  │
│  ║  • Typography (8 font definitions)                           ║  │
│  ║  • Dimensions (card radius, button radius, sidebar width)    ║  │
│  ╚═══════════════════════════════════════════════════════════════╝  │
│                                                                     │
│  ╔═══════════════════════════════════════════════════════════════╗  │
│  ║                     UI LAYER (View)                          ║  │
│  ║                                                               ║  │
│  ║  ┌─────────────────────────────────────────────────────────┐  ║  │
│  ║  │  HEADER — Gradient panel (indigo → purple)              │  ║  │
│  ║  │  • App title + subtitle                                 │  ║  │
│  ║  │  • Theme toggle button (☾/☀)                            │  ║  │
│  ║  └─────────────────────────────────────────────────────────┘  ║  │
│  ║  ┌────────────────┐  ┌─────────────────────────────────────┐  ║  │
│  ║  │   SIDEBAR      │  │         CONTENT AREA                │  ║  │
│  ║  │                │  │                                     │  ║  │
│  ║  │  [Algorithm]   │  │  ┌──────────────────────────────┐   │  ║  │
│  ║  │  [Key Field]   │  │  │  INPUT TEXT AREA              │   │  ║  │
│  ║  │  [Key Size]    │  │  │  (editable, monospaced font)  │   │  ║  │
│  ║  │  [Generate]    │  │  │  Character count: N           │   │  ║  │
│  ║  │                │  │  └──────────────────────────────┘   │  ║  │
│  ║  │  [Load File]   │  │  ┌──────────────────────────────┐   │  ║  │
│  ║  │  [Save File]   │  │  │  OUTPUT TEXT AREA             │   │  ║  │
│  ║  │                │  │  │  (read-only, monospaced font) │   │  ║  │
│  ║  │  [Quick Help]  │  │  │  Character count: N  [Copy]   │   │  ║  │
│  ║  │                │  │  └──────────────────────────────┘   │  ║  │
│  ║  └────────────────┘  │                                     │  ║  │
│  ║                      │  ┌────────┐ ┌──────────┐ ┌───────┐  │  ║  │
│  ║                      │  │Encrypt │ │ Decrypt  │ │ Clear │  │  ║  │
│  ║                      │  └────────┘ └──────────┘ └───────┘  │  ║  │
│  ║                      └─────────────────────────────────────┘  ║  │
│  ║  ┌─────────────────────────────────────────────────────────┐  ║  │
│  ║  │  STATUS BAR — "✓ Ready"                    v2.0        │  ║  │
│  ║  └─────────────────────────────────────────────────────────┘  ║  │
│  ╚═══════════════════════════════════════════════════════════════╝  │
│                                                                     │
│  ╔═══════════════════════════════════════════════════════════════╗  │
│  ║                  EVENT HANDLING LAYER (Controller)           ║  │
│  ║  • updateKeySize()              — live key validation       ║  │
│  ║  • updateInputLength()          — character counter         ║  │
│  ║  • updateKeyFieldForAlgorithm() — algorithm switch logic    ║  │
│  ║  • registerShortcuts()          — Ctrl+E/D/G bindings       ║  │
│  ║  • toggleTheme()                — rebuild UI with new theme ║  │
│  ║  • performEncryption()          — orchestrate encrypt flow  ║  │
│  ║  • performDecryption()          — orchestrate decrypt flow  ║  │
│  ╚═══════════════════════════════════════════════════════════════╝  │
│                                                                     │
│  ╔═══════════════════════════════════════════════════════════════╗  │
│  ║                  CRYPTO ENGINE LAYER (Model)                ║  │
│  ║  • encryptAES() / decryptAES()  — AES-128/CBC/PKCS5 + IV   ║  │
│  ║  • encryptDES() / decryptDES()  — DES-56/CBC/PKCS5 + IV    ║  │
│  ║  • encryptRSA() / decryptRSA()  — RSA-2048/PKCS1           ║  │
│  ║  • generateKey()                — SecureRandom key gen      ║  │
│  ╚═══════════════════════════════════════════════════════════════╝  │
│                                                                     │
│  ╔═══════════════════════════════════════════════════════════════╗  │
│  ║                  UTILITIES LAYER                            ║  │
│  ║  • loadFile() / saveFile()      — file I/O (UTF-8)         ║  │
│  ║  • copyToClipboard()            — system clipboard access   ║  │
│  ║  • clearFields()                — secure wipe with confirm  ║  │
│  ║  • setStatus() / setStatusTimed()  — status bar updates     ║  │
│  ║  • showError()                  — error dialog              ║  │
│  ╚═══════════════════════════════════════════════════════════════╝  │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🚀 Getting Started

### Prerequisites

| Requirement | Minimum | Recommended |
|-------------|---------|-------------|
| **Java JDK** | 8 | 17 or later |
| **OS** | Windows, macOS, or Linux | Any |
| **Display** | 950 × 650 px | 1200 × 780 px or larger |

### Verify Java Installation

```bash
java -version
javac -version
```

### Compile

```bash
cd path/to/miniprojet
javac -encoding UTF-8 CryptoSyst.java
```

### Run

```bash
java CryptoSyst
```

The application will launch with the **dark theme** by default.

---

## 📖 User Guide

### Step 1 — Select an Algorithm

Open the **🔧 Algorithm** dropdown in the left sidebar and choose:

| Algorithm | Best For |
|-----------|----------|
| **AES-128** | Secure, fast encryption of any text (recommended) |
| **DES-56** | Educational experimentation only |
| **RSA-2048** | Short messages, understanding asymmetric crypto |

### Step 2 — Generate or Enter a Key

- Click **⚡ Generate a Key** to create a secure key automatically
- Or paste an existing **Base64-encoded** key into the key field
- The **key size indicator** below the field will show the bit strength
- For **RSA**: keys are auto-generated on first encryption — no manual input needed

### Step 3 — Enter Your Text

- Type or paste text into the **✏️ Input** area
- Or click **📂 Load a File** to import text from disk
- The **character counter** updates in real time

### Step 4 — Encrypt or Decrypt

| Button | Action | Shortcut |
|--------|--------|----------|
| 🔒 **Encrypt** | Transforms plaintext → ciphertext (Base64) | `Ctrl+E` |
| 🔓 **Decrypt** | Transforms ciphertext (Base64) → plaintext | `Ctrl+D` |
| 🗑️ **Clear** | Wipes all fields (with confirmation) | — |

### Step 5 — Retrieve the Result

- The result appears in the **✅ Result** area (read-only)
- Click **📋 Copy** to copy to clipboard
- Click **💾 Save Result** in the sidebar to export to a file

---

## ⌨️ Keyboard Shortcuts

| Shortcut | Action |
|----------|--------|
| `Ctrl + E` | Encrypt the input text |
| `Ctrl + D` | Decrypt the input text |
| `Ctrl + G` | Generate a new key for the selected algorithm |

---

## 🔧 Technical Deep Dive

### Custom UI Rendering

The interface is built with **custom-painted Swing components** — no third-party libraries:

| Component | Rendering Technique |
|-----------|-------------------|
| **Header** | `GradientPaint` drawn in `paintComponent()` — indigo (#6366F1) → purple (#8B5CF6) |
| **Cards** | `RoundRectangle2D.Float` filled + stroked with 14px corner radius |
| **Sidebar buttons** | Custom `paintComponent()` with `RoundRectangle2D` (10px radius), hover brightening |
| **Action buttons** | Pill shape (22px radius) with offset shadow rectangle + hover state |
| **Status bar** | Manual `drawLine()` for top border, background color from theme |
| **Theme toggle** | Rebuilds entire UI via `removeAll()` + `initComponents()` + `revalidate()` |

### Anti-Aliasing

```java
// Enabled globally for smooth text and graphics
System.setProperty("awt.useSystemAAFontSettings", "on");
System.setProperty("swing.aatext", "true");

// Per-component rendering hint
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
```

### Theme System

The app maintains **two complete color palettes** (14 colors total). Every UI component calls helper methods like `getThemeBg()`, `getThemeCard()`, `getThemeText()`, etc., which return colors based on the current `isDarkTheme` boolean.

| Token | Dark Theme | Light Theme |
|-------|-----------|-------------|
| Background | `#16161E` | `#F3F4F9` |
| Surface | `#1E1F2B` | `#FFFFFF` |
| Card | `#262736` | `#FFFFFF` |
| Border | `#37384B` | `#D7DAE6` |
| Text | `#E6E6F0` | `#1E1E2D` |
| Text Secondary | `#9698AA` | `#6E7387` |
| Input Background | `#191A26` | `#F8F9FC` |

### IV Handling Strategy

For AES and DES, the **IV is prepended to the ciphertext** before Base64 encoding:

```
Encrypted output (Base64 decoded):
┌──────────────────┬──────────────────────────────────────┐
│   IV (16 bytes   │        Ciphertext                    │
│   for AES, 8     │        (variable length)             │
│   for DES)       │                                      │
└──────────────────┴──────────────────────────────────────┘

During decryption, the IV is split off:
  iv         = bytes[0 .. ivSize-1]
  ciphertext = bytes[ivSize .. end]
```

This is the standard approach — the IV does not need to be secret, only unique.

---

## 🛡️ Security

### Security Measures Implemented

| # | Measure | Detail |
|:-:|---------|--------|
| 1 | **CBC Mode** | Block chaining prevents pattern leakage (replaces insecure ECB) |
| 2 | **Random IV** | Every encryption produces different output, even with the same key + plaintext |
| 3 | **SecureRandom** | Cryptographically strong RNG for all key and IV generation |
| 4 | **Explicit UTF-8** | `StandardCharsets.UTF_8` used everywhere — no platform-dependent encoding |
| 5 | **Key Validation** | Base64 format and byte-length checks before any crypto operation |
| 6 | **RSA Key Protection** | Private key is never displayed — only a truncated public key is shown |
| 7 | **Memory Cleanup** | RSA key pair is nullified when the user clears all fields |
| 8 | **Input Validation** | Descriptive error messages for empty inputs, wrong key sizes, corrupted data |

---

## ⚠️ Known Limitations

| Limitation | Explanation | Mitigation |
|------------|-------------|------------|
| DES is obsolete | 56-bit keys can be brute-forced in hours | Included for **educational purposes only** — use AES instead |
| RSA size limit | RSA-2048 can only encrypt ~245 bytes | Use RSA for key exchange, not bulk data |
| RSA keys are in-memory only | Private key is lost when the application closes | By design — prevents key leakage to disk |
| Text files only | Binary files are read as text, which may corrupt non-UTF-8 data | Use the app for text; handle binary files externally |
| Single-threaded crypto | Large inputs may briefly freeze the UI | Acceptable for educational use; production apps would use `SwingWorker` |

---

## 📚 Technologies Used

| Technology | Purpose |
|------------|---------|
| **Java SE** | Core language and runtime |
| **Swing (`javax.swing`)** | GUI framework — windows, buttons, text areas, dialogs |
| **AWT (`java.awt`)** | Graphics rendering, colors, fonts, events, gradients |
| **AWT Geom (`java.awt.geom`)** | `RoundRectangle2D` for rounded cards and buttons |
| **JCE (`javax.crypto`)** | `Cipher`, `KeyGenerator` — encryption/decryption engine |
| **JCE Spec (`javax.crypto.spec`)** | `SecretKeySpec`, `IvParameterSpec` — key and IV wrappers |
| **Java Security (`java.security`)** | `SecureRandom`, `KeyPairGenerator`, `KeyPair` — RSA key management |
| **Base64 (`java.util.Base64`)** | Encoding/decoding binary data as printable text |
| **NIO (`java.nio.file`)** | Modern file I/O with `Files.readAllBytes()` / `Files.write()` |
| **NIO Charset** | `StandardCharsets.UTF_8` — explicit character encoding |



<div align="center">

### Built with ☕ Java & 🔐 JCE
  BRITAH ABDESSSAMED M1 data science & AI
**CryptoSyst** — A Encryption & Decryption System

*Mini-Project • Information Security Course*

</div>
