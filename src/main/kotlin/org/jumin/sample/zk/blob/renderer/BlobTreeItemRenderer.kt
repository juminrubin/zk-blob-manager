package org.jumin.sample.zk.blob.renderer

import com.azure.storage.blob.models.BlobItem
import org.zkoss.zk.ui.event.Events
import org.zkoss.zul.*
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

class BlobTreeItemRenderer : TreeitemRenderer<BlobItem> {

    companion object {

        val ON_DIRECTORY_TOGGLE = "onDirectoryToggle"

        val SIZE_FORMATTER: NumberFormat = NumberFormat.getIntegerInstance(Locale.ENGLISH)
        val TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

        fun getTreeColumns(): Treecols {
            val treeCols = Treecols()
            treeCols.isSizable = true
            treeCols.appendChild(Treecol("", null, "40px"))
            treeCols.appendChild(Treecol("Name"))
            val sizeCol = Treecol("Size (Bytes)", null, "200px")
            sizeCol.align = "right"
            treeCols.appendChild(sizeCol)
            treeCols.appendChild(Treecol("Modify Date", null, "240px"))
            treeCols.appendChild(Treecol("Blob Type", null, "160px"))

            return treeCols
        }
    }

    override fun render(item: Treeitem?, data: BlobItem?, index: Int) {
        if (item == null) return

        val row = Treerow()
        row.parent = item
        if (data == null) {
            return
        }
        row.appendChild(Treecell(""))
        row.appendChild(Treecell(data.name))
        if (data.properties == null) {
            row.appendChild(Treecell(""))
            row.appendChild(Treecell(""))
            row.appendChild(Treecell(""))
        } else {
            row.appendChild(Treecell(SIZE_FORMATTER.format(data.properties.contentLength)))
            val zoned = data.properties.lastModified.atZoneSameInstant(TimeZone.getDefault().toZoneId())
            row.appendChild(Treecell(TIMESTAMP_FORMATTER.format(zoned.toLocalDateTime())))
            row.appendChild(Treecell(data.properties.blobType.name))
        }
        if (data.isPrefix) {
            item.appendChild(Treechildren())
            // forward
            item.addForward(Events.ON_CLICK, row.tree, ON_DIRECTORY_TOGGLE, item)
            item.addForward(Events.ON_DOUBLE_CLICK, row.tree, ON_DIRECTORY_TOGGLE, item)
            item.addForward(Events.ON_OPEN, row.tree, ON_DIRECTORY_TOGGLE, item)
        }
        item.isOpen = false
        item.setValue(data)
    }
}