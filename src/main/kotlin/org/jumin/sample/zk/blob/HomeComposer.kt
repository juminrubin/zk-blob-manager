package org.jumin.sample.zk.blob

import com.azure.storage.blob.models.BlobItem
import com.azure.storage.blob.models.ListBlobsOptions
import org.jumin.sample.zk.blob.renderer.BlobTreeItemRenderer
import org.jumin.sample.zk.blob.utils.BlobStorageUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.zkoss.zk.ui.event.Events
import org.zkoss.zk.ui.event.ForwardEvent
import org.zkoss.zk.ui.event.MouseEvent
import org.zkoss.zk.ui.util.GenericForwardComposer
import org.zkoss.zul.*
import java.io.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class HomeComposer : GenericForwardComposer<Vlayout>() {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private val blobSasUrl: Textbox? = null

    private val blobContentTree: Tree? = null

    private val listContentButton: Button? = null

    private val nf: NumberFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH)

    private val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    private val treeItemRenderer: BlobTreeItemRenderer = BlobTreeItemRenderer()

    @Throws(Exception::class)
    override fun doAfterCompose(comp: Vlayout) {
        super.doAfterCompose(comp)

        initWidgets()
        initEvents()
    }

    private fun initEvents() {
        blobSasUrl?.addForward(Events.ON_OK, listContentButton, Events.ON_CLICK)

        blobContentTree?.addEventListener(BlobTreeItemRenderer.ON_DIRECTORY_TOGGLE) { event ->
            if (event.data == null || Treeitem::class.java != event.data.javaClass) {
                return@addEventListener
            }


            val clickedTreeItem = event.data as Treeitem
            if (clickedTreeItem.treechildren.lastChild == null) {
                // load Treechildren
                loadDirectoryContent(clickedTreeItem)
            }

            val originEvent = (event as ForwardEvent).origin
            if (!Events.ON_OPEN.equals(originEvent.name, true)) {
                clickedTreeItem.isOpen = !clickedTreeItem.isOpen
            }
        }
    }

    private fun loadDirectoryContent(directoryTreeItem: Treeitem) {
        if (blobSasUrl == null ||
            blobSasUrl.value.isEmpty() ||
            blobContentTree == null ||
            directoryTreeItem == null ||
            BlobItem::class.java != directoryTreeItem.getValue<Any?>().javaClass
        ) return

        val containerClient = BlobStorageUtils.getBlobContainerClient(blobSasUrl.value)
        val blobItem: BlobItem = directoryTreeItem.getValue()
        val delimiter = "/"
        val options: ListBlobsOptions = ListBlobsOptions().setPrefix("${blobItem.name}")
        containerClient.listBlobsByHierarchy(delimiter, options, null)
            .forEachIndexed { index, subBlobItem ->
                val treeItem = Treeitem()
                treeItem.parent = directoryTreeItem.treechildren

                treeItemRenderer.render(treeItem, subBlobItem, index)
            }
    }

    fun initWidgets() {
        if (blobContentTree != null) {
            if (blobContentTree.treecols != null) {
                blobContentTree.removeChild(blobContentTree.treecols)
            }

            blobContentTree.appendChild(BlobTreeItemRenderer.getTreeColumns())
            blobContentTree.setItemRenderer(treeItemRenderer)

            // add root tree children node if not exist
            if (blobContentTree.treechildren == null) {
                blobContentTree.appendChild(Treechildren())
            }
        }

        blobSasUrl?.value = System.getProperty("containerSasKey", "")
    }

    fun `onClick$listContentButton`(event: MouseEvent) {
        if (blobSasUrl == null || blobSasUrl.value.isEmpty() || blobContentTree == null) return

        logger.info("Show contents")

        val containerClient = BlobStorageUtils.getBlobContainerClient(blobSasUrl.value)

        // Clear up tree
        if (blobContentTree.treechildren.lastChild != null) {
            clearTreechildren(blobContentTree.treechildren)
        }

        containerClient.listBlobsByHierarchy("")
            .forEachIndexed { index, blobItem ->
                logger.info("#${index} rendering blob: ${blobItem.name}")
                val treeItem = Treeitem()
                treeItem.parent = blobContentTree.treechildren

                treeItemRenderer.render(treeItem, blobItem, index)
            }
    }

    private fun clearTreechildren(treechildren: Treechildren) {
        while (treechildren.lastChild != null) {
            treechildren.removeChild(treechildren.lastChild)
        }
    }
}