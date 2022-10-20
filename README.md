# Created a App to fetch movies List from remote server and show it on the screen.
 Created a detail page of any movie that is being clicked <br /> 
 Also handled case where data is being saved in roomdatabase and will be fetched in case of failure.<br /> 
 Created a Search bar where you can search specific movie from list.<br /> 
 Created a BookMarkFragment where bookmark elements are being shown.<br /> 




Detail description of the packages that are being created:-<br /> 
   1)UI-> contains information about the activity and fragments that are being used in this project.<br /> 
         MainActivity-> activity where list of movies items are to be shown.<br /> 
         DetailActivity -> activity where detailed information about any particular movie is being shown.<br /> 
         BookMarkFragment -> a bottom sheet fragment where all the bookmark items are being shown. <br /> 
   2) adapter -> all the adapters that are being used on movies listing,detail or bookmark page.<br /> 
   3) base -> base classes that are being used inside project <br /> 
   4) binding -> BindingAdapter implementation is wriiten there (used to set items in recyclerview + used to fetch images through picasso)<br /> 
   5) custom -> verticalItemDecorator is used to give vertical margin b/w the items of recyclerview<br /> 
   6) database -> all infos about the databases are written there which includes entity,dao,rommdatabase info.<br /> 
   7) event -> these are basically string constants used while sending events from viewModel to activity<br /> 
   8) factory -> viewmodelfactory is impemented here.<br /> 
   9) helper -> helper classes implemetation is written here<br /> 
   10) model -> data classes that is being required for fetching response from remote server are written here.<br /> 
   11) netwrok -> network layer implementation is written here.(retrofit)<br /> 
   12) viewModel -> all the viewModels and their implementation are written here.<br /> 





abstract class HotelBaseRecyclerAdapter(private val itemList: MutableList<AbstractRecyclerItem>)
    : RecyclerView.Adapter<HotelRecyclerViewHolder<in ViewDataBinding, in AbstractRecyclerItem>>() {

    private var animator: HotelItemAnimator? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelRecyclerViewHolder<in ViewDataBinding, in AbstractRecyclerItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return getViewHolder(viewType, layoutInflater, parent)
    }


    protected abstract fun getViewHolder(viewType: Int, layoutInflater: LayoutInflater, parent: ViewGroup):
            HotelRecyclerViewHolder<in ViewDataBinding, in AbstractRecyclerItem>

     override fun getItemCount(): Int {
        return itemList.size
    }

    fun getItems():List<AbstractRecyclerItem> {
        return itemList
    }

    override fun onBindViewHolder(holder: HotelRecyclerViewHolder<in ViewDataBinding, in AbstractRecyclerItem>, position: Int) {
        holder.bindData(getItem(position), position)
        animator?.animate(holder,position)
    }

   final  override fun getItemViewType(position: Int): Int {
        return getItem(position).getItemType()
    }

   open fun getItem(position: Int) : AbstractRecyclerItem {
        if(position < 0 || position > itemCount) {
            throw IndexOutOfBoundsException()
        } else {
            return itemList[position]
        }
    }

    fun setItemAnimator(animator: HotelItemAnimator?){
        this.animator = animator
    }

    fun addItem(position: Int, item: AbstractRecyclerItem) {
        try {
            itemList.add(position, item)
        } catch (e: Exception) {
            LogUtils.error("TAG", "IndexOutOfBoundsException")
        }
        notifyItemInserted(position)
    }

    fun addItems(items : List<AbstractRecyclerItem>, addAtStart : Boolean = false) {
        if(addAtStart) {
            itemList.addAll(0,items)
        } else {
            itemList.addAll(items)
        }
        notifyDataSetChanged()
    }

    fun removeItem(item: AbstractRecyclerItem) {
        if( !itemList.contains(item) )
            return
        val position: Int = itemList.indexOf(item)
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItem(item: AbstractRecyclerItem){
        itemList.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun modifyItem(position: Int, item: AbstractRecyclerItem) {
        if (itemList.size <= position) {
            return
        }
        itemList[position] = item
        notifyItemChanged(position)
    }

    fun modifyItem(item: AbstractRecyclerItem) {
        val position = itemList.indexOf(item)
        modifyItem(position, item)
    }

    fun updateList(dataList: List<AbstractRecyclerItem>, ignoreEmptyList: Boolean = true) {
        itemList.clear()
        if (dataList.isEmpty() && ignoreEmptyList) {
            return
        }
        itemList.addAll(dataList)
        notifyDataSetChanged()
    }

    /**
     * This will not notify adapter after updating list. This method is useful
     * when notifying list update thorough diff utils
     * */
    open fun updateListOnly(dataList: List<AbstractRecyclerItem>){
        itemList.clear()
        itemList.addAll(dataList)
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }
}

//

abstract class HotelRecyclerViewHolder<T : ViewDataBinding, V>(layoutInflater: LayoutInflater, layoutId: Int, parent: ViewGroup)
    : AbstractLiveDataViewHolder<T>(DataBindingUtil.inflate(layoutInflater,
        layoutId, parent, false)) {

    abstract fun bindData(data: V, position: Int)

    open fun bindData(data: V, position: Int, payLoad: MutableList<Any>) {

    }

    open fun onViewAttachedToWindow(){

    }

    open fun onViewDetachedFromWindow(){

    }
}

//activity
abstract class HotelActivity<T : HotelViewModel, V : ViewDataBinding> : LocaleAppCompatActivity() {
    protected var apiStartTime: Long = 0
    protected var isApiTracked = false
    lateinit var viewDataBinding: V
    abstract fun createViewModel(): T
    abstract fun createEventSharedViewModel(): HotelEventSharedViewModel

    /**
     * this method is called before super.onCreate() , it must only be used for data which is required for dependency injection
     */
    open fun initAndValidate() {}

    companion object {
        const val TAG = "HotelActivity"
    }

    val viewModel: T by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        createViewModel()
    }

    val eventSharedViewModel by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        createEventSharedViewModel()
    }

    override fun getLob() = LOBS.HOTEL

    private val configUtil = HotelConfigUtil()
    val createTrace = "${javaClass.simpleName}_CreateTrace"
    val trace = FirebasePerformance.startTrace(createTrace)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        initAndValidate()
        super.onCreate(savedInstanceState)
        registerOnBackPressedDispatcher()
        initExtraDataLoggingForFabric()
        syncMobConfig()
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel.eventStream.observe(this, Observer {
            handleEvents(it)
        })
        eventSharedViewModel.eventStream.observe(this, Observer {
            handleSharedEvents(it)
        })
    }

    private fun syncMobConfig() {
        if (mobConfigResultRequired()) {
            configUtil.bindService(this, resultReceiver)
        }else{
            configUtil.startService(this)
        }
    }

    fun clearFragmentBackStack() {
        try {
            for (i in 0 until supportFragmentManager.backStackEntryCount) {
                supportFragmentManager.popBackStack()
            }
        } catch (exception: Exception) {

        }
    }

    abstract fun getLayoutId(): Int

    abstract fun handleEvents(event: HotelEvent)

    fun handleSharedEvents(event: HotelEvent) {
        when(event.eventID) {
            HotelEvents.PAGE_LOADED -> {
                stopTrace()
            }
            HotelEvents.TRACE_PUT_METRIC -> {
                event.data.executeIfCast<FirebaseMetric> {
                    putMetric(this)
                }
            }
            HotelEvents.API_REQUEST_STARTED -> {
                firebaseApiTracking(true)
            }
            HotelEvents.API_REQUEST_FINISHED -> {
                firebaseApiTracking(false)
            }
            else -> {
                handleEvents(event)
            }
        }
    }

    fun putMetric(metric: FirebaseMetric) {
        trace.putMetric(metric.name, metric.value)
    }

    fun stopTrace() {
        trace.stop()
    }

    /**
     * MobConfig api result will send on successful call
     * or empty bundle if data already synced
    * */
    open fun onMobConfigResultReceived(resultData: Bundle?) {}
    /**
     * return true if mob config result required of successful call
     *
     * */
    open fun mobConfigResultRequired() = false

    private fun registerOnBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback(this, object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                onHandleBackPress()
            }
        })
    }

    open fun onHandleBackPress() {
        try {
            if (!isFinishing && supportFragmentManager.backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        } catch (e: Exception) {

        }
    }

    protected fun initExtraDataLoggingForFabric() {
        initFabricWithDefault(this.javaClass.simpleName)
    }

    open fun launchCosmosWebView(title: String? = null, url: String?, launchFrom: Int = -1,showHeader: Boolean = true) {
            val webViewBundle = WebViewBundle(
                isFinishOnBack = true,
                webViewTitle = title ?: EMPTY_STRING,
                webViewUrl = url,
                source = launchFrom, showHeader = showHeader
            )
        launchCosmosWebView(webViewBundle)
    }

    protected fun launchCosmosWebView(webViewBundle: WebViewBundle){
        if (preConditionsMet(webViewBundle.webViewUrl)) {
            val intent = Intent(CoreConstants.ACTION_LAUNCH_WEB_VIEW_COSMOS)
            val bundle = Bundle()
            bundle.putParcelable(CoreConstants.INTENT_BUNDLE, webViewBundle)
            intent.putExtras(bundle)
            GenericUtils.startActivityInternal(this, intent)
        }
    }

    private fun preConditionsMet(webViewUrl: String?): Boolean {
        if (!isNetworkAvailable(this)) {
            ResourceProvider.instance.showToast(ResourceProvider.instance.getString(R.string.htl_NETWORK_ERROR_MSG), Toast.LENGTH_SHORT)
            return false
        }
        if (webViewUrl.isNullOrEmpty()) {
            ResourceProvider.instance.showToast(ResourceProvider.instance.getString(R.string.htl_SOMETHING_WENT_WRONG), Toast.LENGTH_SHORT)
            return false
        }
        return true
    }

    fun getTopFragment(): Fragment? {
        val currentFragmentTag = getTopFragmentTag()
        return supportFragmentManager.findFragmentByTag(currentFragmentTag)
    }

    fun getTopFragmentTag(): String? {
        val index = supportFragmentManager.backStackEntryCount - 1
        return if (index >= 0) {
            supportFragmentManager.getBackStackEntryAt(index).name
        } else {
            null
        }
    }

    override fun onStop() {
        try {
            viewModel.clearEventStream()
            eventSharedViewModel.clearEventStream()
            super.onStop()
        } catch (e: Throwable) {
            LogUtils.error(TAG, e)
            try {
                //this is fix for firebase performance crashes
                //https://stackoverflow.com/questions/55352210/illegalargumentexception-attempt-to-remove-onframemetricsavailablelistener-that
                super.onStop()
            } catch (e2: Throwable) {
                LogUtils.error(TAG, e2)
                throw e2
            }
        }
    }

    override fun onDestroy() {
        configUtil.unbindService(this)
        super.onDestroy()
    }

    private val resultReceiver = object : ResultReceiver(Handler(Looper.getMainLooper())) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)
            onMobConfigResultReceived(resultData)
        }
    }

    inline fun <reified X : HotelViewModel, reified Y : ViewDataBinding> addBottomFragment(fragment: HotelFragment<X, Y>, tag: String) {
        try{
            hideKeyboard(this)
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.slide_up, R.animator.slide_down, R.animator.slide_up, R.animator.slide_down)
                    .add(R.id.bottom_frag_container, fragment, tag)
                    .addToBackStack(tag)
                    .commitAllowingStateLoss()
        }catch (exception:Exception) {

        }
    }

    inline fun <reified X : HotelViewModel, reified Y : ViewDataBinding> addBottomFragment(containerID : Int, fragment: HotelFragment<X, Y>, tag: String) {
        try{
            hideKeyboard(this)
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.slide_up, R.animator.slide_down, R.animator.slide_up, R.animator.slide_down)
                    .add(containerID, fragment, tag)
                    .addToBackStack(tag)
                    .commitAllowingStateLoss()
        }catch (exception:Exception) {

        }
    }

    inline fun <reified X : HotelViewModel, reified Y : ViewDataBinding> replaceChildFragment(fragment: HotelFragment<X, Y>, tag: String,
                                                                                              layoutId: Int = -1,addToBackStack:Boolean = true) {
        try {
            hideKeyboard(this)
            val fragmentContainerID = if(layoutId!=-1){
                layoutId
            }else{
                R.id.child_frag_container
            }
            val transaction = supportFragmentManager.beginTransaction()
                .replace(fragmentContainerID, fragment, tag)

            if (addToBackStack) {
                transaction.addToBackStack(tag)
            }
            transaction.commitAllowingStateLoss()
            supportFragmentManager.executePendingTransactions()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    /**
     * apiStart value true if apiRequest Started, false if request ended
     */
    protected fun firebaseApiTracking(apiStart: Boolean) {
        LogUtils.debug("firebaseTracking","time:${System.currentTimeMillis()}")
        if (!isApiTracked) {
            if (apiStart) {
                apiStartTime = System.currentTimeMillis()
            } else if (apiStartTime > 0) {
                putMetric(
                    FirebaseMetric(
                        FirebaseConstants.METRIC_SEARCH_API,
                        System.currentTimeMillis() - apiStartTime
                    )
                )
                isApiTracked = true
            }
        }
    }
}

//bottom up fragment
abstract class HotelBottomUpFragment<T: HotelViewModel, V : ViewDataBinding> : HotelFragment<T, V>() {
    abstract val backgroundViewId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(canAnimateStatusBar()){
            FragmentAnimUtil.animStatusBar(activity, R.color.white, R.color.transparent_black, 300)
        }
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return FragmentAnimUtil.slideUpAnimationAndDimBg(activity, enter, nextAnim, backgroundViewId)
    }

    override fun shouldInterceptBackPress(): Boolean {
        return true
    }

    override fun onDestroyView() {
        if(canAnimateStatusBar()){
            FragmentAnimUtil.animStatusBar(activity, R.color.transparent_black, R.color.white, 300)
        }
        super.onDestroyView()
    }

    open fun canAnimateStatusBar():Boolean {
        return true
    }
}

// fragment

abstract class HotelFragment<T : HotelViewModel, V : ViewDataBinding> : Fragment() {

    lateinit var viewDataBinding: V
    abstract fun  initViewModel():T
    open fun injectDependency() {}

    /**
     * this method is called in onAttach , it must only be used for data which is required for dependency injection
     */
    open fun initAndValidate() {}
    private  var onBackPressedCallback: OnBackPressedCallback? = null

    val viewModel: T by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        initViewModel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependency()
        initAndValidate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addOnBackPressedDispatcher()
        initExtraDataLoggingForFabric()
        LogUtils.debug("HotelFragment","OnCreate()..:"+javaClass.simpleName)
    }

    private fun addOnBackPressedDispatcher() {
        if(!shouldInterceptBackPress()) {
            return
        }
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onHandleBackPress()
            }
        }
    }

    /**
     * registering [onBackPressedCallback] in [onResume] as , when apps comes
     * from background, activity is getting registered to stack at last and will consume
     * backpressed event
     */
    override fun onResume() {
        super.onResume()
        onBackPressedCallback?.let {
            activity?.onBackPressedDispatcher?.addCallback(this, it)
        }
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback?.remove()
    }

    open fun onHandleBackPress() {
        onBackPressedCallback?.isEnabled = false
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    open fun shouldInterceptBackPress() = false

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setWindowProperties()
        viewDataBinding = DataBindingUtil.inflate(getThemeInflater(inflater), getLayoutId(), container, false)
        setDataBinding()
        initFragmentView()
        viewDataBinding.root.isClickable = true
        return viewDataBinding.root
    }

    private fun getThemeInflater(inflater: LayoutInflater): LayoutInflater {
        val themeId = setTheme()
        if(themeId == -1){
            return inflater
        }
        activity?.let {
            val contextThemeWrapper = ContextThemeWrapper(it, themeId)
            val themedInflater = inflater.cloneInContext(contextThemeWrapper);
            return themedInflater
        }
        return inflater
    }

    open fun setTheme() = -1

    open fun setWindowProperties(){

    }

    abstract fun setDataBinding()

    abstract fun getLayoutId(): Int

    abstract fun initFragmentView()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.eventStream.observe(viewLifecycleOwner, {
            handleEvents(it)
        })
    }

    fun isViewDataBindingInitialised() = ::viewDataBinding.isInitialized

    protected open fun dismissFragment() {
        performIfActivityActive(activity) { activity ->
            (activity as FragmentActivity).supportFragmentManager.popBackStack()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.clearEventStream()
    }


    /**
     * method for showing simple ui msgs in  bottom sheet
     * use this if bottom sheet requires only title , msg and cross button
     */
    protected fun showBottomSheet(title: String, items: List<LinearLayoutItemData>, showCrossIcon: Boolean = true) {
        this.context?.let {
            val bottomSheet = HotelBottomSheetDialog(HotelBottomSheetInfoUiData(title, showCrossIcon, items), it)
            bottomSheet.getLiveData().observe(this, Observer { event ->
                when (event.eventID) {
                    HotelBottomSheetEvents.DISMISS_BOTTOM_SHEET -> {
                        bottomSheet.dismiss()
                    }
                }
            })
            bottomSheet.show()
        }
    }

    abstract fun handleEvents(event: HotelEvent)

    protected fun initExtraDataLoggingForFabric() {
        initFabricWithDefault(this.javaClass.simpleName)
    }

    protected fun setLightStatusBar(){
        HotelUtil.setLightStatusBar(activity)
    }

}
