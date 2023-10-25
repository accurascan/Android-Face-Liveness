# Liveness

## Install Accura Face liveness SDK in to your App

#### Step 1: Add the JitPack repository to your build file:
    Add it in your root build.gradle at the end of repositories.

    allprojects {
        repositories {
            ...
            maven {
                url 'https://jitpack.io'
                credentials { username authToken }
            }
        }
    }

#### Step 2. Add the token to `gradle.properties`:

    authToken=jp_ssguccab6c5ge2l4jitaj92ek2

#### Step 3: Add the dependency:<br />
    Set Accura Liveness as a dependency to our app level `build.gradle` file.

    dependencies {
        ...
        // for liveness
        implementation 'com.github.accurascan:Liveness-Android:3.3.1'
    }

## Setup Accura Liveness

    Contact AccuraScan at contact@accurascan.com for Liveness SDK or API

#### Step 1 : Add following code in Manifest.

    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.INTERNET" />

    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />

    <application
        ...
        android:networkSecurityConfig="@xml/network_security_config"
        >

     </application>

#### Step 2 :  Add following code to your Application class or MainActivity for hostname verification

    new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify("your url host name", session);
        }
    };

#### Step 3 : Open camera for liveness Detectcion.

    Must to Grant camera permission

    private final static int ACCURA_LIVENESS_CAMERA = 100;

    // To customize your screen theme and feed back messages
    LivenessCustomization livenessCustomization = new LivenessCustomization();

    livenessCustomization.backGroundColor = getResources().getColor(R.color.livenessBackground);
    livenessCustomization.closeIconColor = getResources().getColor(R.color.livenessCloseIcon);
    livenessCustomization.feedbackBackGroundColor = Color.TRANSPARENT;
    livenessCustomization.feedbackTextColor = Color.BLACK;
    livenessCustomization.feedbackTextSize = 18;
    livenessCustomization.feedBackTopMessage = "Keep Face In Frame \n Face Must Be Near To Camera ";
    livenessCustomization.feedBackframeMessage = "Frame Your Face";
    livenessCustomization.feedBackAwayMessage = "Move Phone Away";
    livenessCustomization.feedBackOpenEyesMessage = "Keep Your Eyes Open";
    livenessCustomization.feedBackCloserMessage = "Move Phone Closer";
    livenessCustomization.feedBackCenterMessage = "Move Phone Center";
    livenessCustomization.feedBackMultipleFaceMessage = "Multiple Face Detected";
    livenessCustomization.feedBackHeadStraightMessage = "Keep Your Head Straight";
    livenessCustomization.feedBackLowLightMessage = "Low light Detected";
    livenessCustomization.feedBackBlurFaceMessage = "Blur Detected Over Face";
    livenessCustomization.feedBackGlareFaceMessage = "Glare Detected over face";
    livenessCustomization.setApiKey("add your api Key");

    // must have to call SelfieCameraActivity.getCustomIntent() to create intent
    Intent intent = SelfieCameraActivity.getCustomIntent(this, livenessCustomization, "your_url");
    startActivityForResult(intent, ACCURA_LIVENESS_CAMERA);


    // Handle accura liveness result.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ACCURA_LIVENESS_CAMERA && data != null) {
                AccuraVerificationResult result = data.getParcelableExtra("Accura.liveness");
                if (result == null) {
                    return;
                }
                if (result.getStatus().equals("1")) {
                    // result get bitmap of face by using following code
                    Bitmap bitmap = result.getFaceBiometrics();
                    double livenessScore = result.getLivenessResult().getLivenessScore() * 100.0;
                    Toast.makeText(this, "Liveness Score : " + livenessScore, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, result.getStatus() + " " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

