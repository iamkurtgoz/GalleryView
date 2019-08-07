# Gallery View
### GalleryView is a simple gallery library. With customized design, you can select image, video and gif file without needing "intent".

[![](https://jitpack.io/v/iamkurtgoz/GalleryView.svg)](https://jitpack.io/#iamkurtgoz/GalleryView)

## Setup
### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
### Step 2. Add the dependency
```
dependencies {
    implementation 'com.github.iamkurtgoz:GalleryView:X:X'
}
```
### Step 3. Add read permission
```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

### Step 4. Add implement listener
```
public class MainActivity extends AppCompatActivity implements GalleryViewListener {
    @Override
    public void onReadyMedia(MediaModel mediaModel, String mediaType, String mediaTypeDetail) {
        Toast.makeText(this, mediaModel.getFile().getAbsolutePath() + "\n" + mediaType + "\n" + mediaTypeDetail, Toast.LENGTH_SHORT).show();
    }
    
    ....
```
### Step 5. And finish..
```
FragmentManager fm = getSupportFragmentManager();
FragmentTransaction tr = fm.beginTransaction();
tr.replace(R.id.activity_main_container, GalleryViewFragment.with(GalleryViewFragment.ONLY_IMAGE, MainActivity.this));
tr.commitAllowingStateLoss();
```
### Contact : iamkurtgoz@gmail.com
