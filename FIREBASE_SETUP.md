# Firebase Setup Guide

Use this app with your own Firebase project by following these steps.

## 1. Create Firebase project

1. Open Firebase Console.
2. Create a new project for `Sai Metal And Furniture`.
3. Add an Android app with package name:
   - `com.saimetal.furniture`

## 2. Add Android config

1. Download `google-services.json`.
2. Place it inside:
   - `app/google-services.json`

## 3. Enable Firebase services

Turn on these services:

- Firebase Authentication
- Cloud Firestore
- Firebase Storage

Recommended sign-in methods:

- Email/password for admin users

## 4. Switch the project to live Firebase mode

In [`app/build.gradle.kts`](C:\Users\bhavi\OneDrive\Documents\New project\app\build.gradle.kts), change:

```kotlin
buildConfigField("boolean", "FIREBASE_ENABLED", "false")
```

to:

```kotlin
buildConfigField("boolean", "FIREBASE_ENABLED", "true")
```

Then, if you want full Firebase auto-configuration, add the Google Services plugin back to the app module.

## 5. Firestore collections to create

Suggested collections:

- `gallery_items`
- `services`
- `inquiries`
- `clients`
- `projects`
- `billings`
- `payments`
- `admins`

## 6. Suggested Firestore rules

High-level rules:

- Public users can read gallery and services
- Public users can create inquiries
- Only authenticated admins can write gallery, projects, billing, and payments

## 7. Next implementation step

This scaffold currently uses local demo data until you enable Firebase, so the UI can be developed immediately.

Next coding step:

- Firebase Auth is already prepared for admin login through `FirebaseSaiRepository.signInAdmin`
- Firestore reads and writes are already prepared for inquiries, gallery items, and dashboard loading
- Next recommended upgrade is Firebase Storage for real project image upload
