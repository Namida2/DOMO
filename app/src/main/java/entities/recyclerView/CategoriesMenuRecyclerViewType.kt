package entities.recyclerView

class CategoriesMenuRecyclerViewType
//    MenuRecyclerViewType<LayoutCategoriesContainerBinding, CategoriesNameHolder>
{

//    override fun getViewHolder(
//        inflater: LayoutInflater,
//        parent: ViewGroup,
//    ): BaseViewHolder<LayoutCategoriesContainerBinding, CategoriesNameHolder> =
//        CategoriesMenuViewHolder(
//            LayoutCategoriesContainerBinding.inflate(inflater, parent, false)
//        )
//
//    override fun getLayoutId(): Int = R.layout.layout_categories_container
//    override fun getDiffCallback(): DiffUtil.ItemCallback<CategoriesNameHolder> = diffCallback
//
//    private val diffCallback = object : DiffUtil.ItemCallback<CategoriesNameHolder>() {
//        override fun areItemsTheSame(
//            oldItem: CategoriesNameHolder,
//            newItem: CategoriesNameHolder,
//        ): Boolean = oldItem == newItem
//
//        override fun areContentsTheSame(
//            oldItem: CategoriesNameHolder,
//            newItem: CategoriesNameHolder,
//        ): Boolean = oldItem == newItem
//
//    }
//
//    override fun isItMe(recyclerViewItem: BaseRecyclerViewItem): Boolean =
//        recyclerViewItem is CategoriesNameHolder
//
//}
//
//class CategoriesMenuViewHolder(
//    override val binding: LayoutCategoriesContainerBinding,
//) : BaseViewHolder<LayoutCategoriesContainerBinding, CategoriesNameHolder>(binding) {
//
//    private var itemsAdapter = MenuItemsAdapter(
//        listOf(
//            CategoryRecyclerViewType()
//        )
//    )
//
//    init {
//        with(binding.containerRecyclerView) {
//            adapter = itemsAdapter
//            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
//            setHasFixedSize(true)
//        }
//    }
//
//    override fun onBind(item: CategoriesNameHolder) {
//        itemsAdapter.submitList(item.categories)
//    }
}