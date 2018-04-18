# ImageLoaderProcessor
> ## 优雅的实现Android主流图片框架封装，可无缝侵入切换图片框架

**这个库主要是解决项目过程中不同人使用不同的图片框架，导致项目开发难度增大，为了统一和平衡，就写了这么一个隔离型框架，可以快速切换图片框架，调用的地方不会有什么影响。这才是这个库最有意思的地方。**

## 使用方式如下：


> **一、首先在application里面声明使用哪个框架**

	public class MyApp extends Application{
	    @Override
	    public void onCreate() {
		super.onCreate();

		//这里只需要一行代码切换图片加载框架，6不6！！！

		//初始化Picasso方式加载图片
		ImageLoaderHelper.setImageLoader(new PicassoLoaderProcessor(new LoaderOptions()));

		//初始化Glide方式网络请求代理
		//ImageLoaderHelper.setImageLoader(new GlideLoaderProcessor());
	    }
	}


> **二、在代码里面具体使用***

**这里只是一个简单的使用，这个库只是提供一种思想，具体的细致的封装可以自己进一步去实现。**

	public class MainActivity extends AppCompatActivity {

	    private ImageView imageView;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageView = (ImageView) findViewById(R.id.iv);

		//真正的加载图片的操作
		ImageLoaderHelper.getsInstance()
			.loadImage(imageView,R.mipmap.ic_launcher)//参数1为控件，参数2为要下载的图片
			.setLoaderOptions(new LoaderOptions.Builder()//设置图片具体的参数
				.angle(2)//角度
				.centerCrop()//填充方式，它与centerInside不能同时使用
				//.centerInside()
				.config(Bitmap.Config.RGB_565)
				.error(R.drawable.ic_holder)//加载失败显示的图片
				.placeHolder(R.drawable.ic_holder)//占位图
				.reSize(200,200)
				.build())
			.clearDiskCache()//清理缓存
			.clearMemoryCache();//清理内存缓存
	    }
	}

-------------------------
是不是很简单？
