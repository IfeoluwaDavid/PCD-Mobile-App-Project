package ifeoluwa.partscribber;

/**
 * Created by Ifeoluwa David on 2017-10-14.
 */

public class Item
{
    private String itemName, serialNo, itemCategory;
    private int itemID, qtyAvailable, qtyRented, qtyTotal;

    public Item(int itemID, String itemName, String serialNo, int qtyAvailable, int qtyRented, int qtyTotal, String itemCategory)
    {
        this.itemID = itemID;
        this.itemName = itemName;
        this.serialNo = serialNo;
        this.qtyAvailable = qtyAvailable;
        this.qtyRented = qtyRented;
        this.qtyTotal = qtyTotal;
        this.itemCategory = itemCategory;
    }

    public int getItemID()
    {
        return itemID;
    }

    public String getItemName()
    {
        return itemName;
    }

    public String getSerialNo()
    {
        return serialNo;
    }

    public int getQtyAvailable()
    {
        return qtyAvailable;
    }

    public int getQtyRented()
    {
        return qtyRented;
    }

    public int getQtyTotal()
    {
        return qtyTotal;
    }

    public String getItemCategory()
    {
        return itemCategory;
    }
}
