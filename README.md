# Sai Metal and Furniture

Native Android app scaffold for Sai Metal And Furniture, built with Kotlin and Jetpack Compose.

Current app modules:

- Customer home screen with premium business presentation
- Services listing for metal and furniture work
- Gallery screen for completed projects
- Favorites screen for saved inspirations
- Quote request screen for new client inquiries
- About screen for trust-building business details
- Billing overview screen for owners
- Admin login and owner dashboard
- Local demo data for works, leads, and billing records
- Firebase-backed repository code for Auth, Firestore, and future Storage integration

Project path:

- `app/src/main/java/com/saimetal/furniture`

Open in Android Studio:

1. Open this folder as a project.
2. Let Gradle sync.
3. Add your own `google-services.json` into the `app/` folder when ready.
4. Change `FIREBASE_ENABLED` in `app/build.gradle.kts` to `true` after Firebase setup is complete.
5. Create an admin user in Firebase Authentication to replace the demo login.

Demo admin login:

- Username: `owner@sai`
- Password: `123456`
