# OpenAll

> OpenAll use is simplify the code to open the component in Android.

But now only support open activity in activity.

[中文介绍](https://juejin.im/post/5a5db8a46fb9a01cc026404c)

### Usage method:

### Step 1:

    @OpenActivity(target = {TargetOneActivity.class, TargetTwoActivity.class})
    public class NowActivity extends Activity {}

Then build, OpenAll is can auto create class NowActivity$$OpenAll, And auto create
 jump target activity code.
 
### Step 2:

    OpenAll.getInstance().open(this, TargetActivity.class);

or

    OpenAll.getInstance().addIntParam("one", 1)
    .addIntParam("two", 2)
    .addIntParam("three", 3)
    .addStringParam("four", "success")
    .open(this, TargetActivity.class);