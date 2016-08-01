package edu.iit.cs442.team7.iitbazaar;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */

public class Category {


    private int category_number;
    private int parent_category_number;
    private String category_name;


    public int getCategoryNumber() {
        return category_number;
    }

    public void setCategoryNumber(final int category_number) {
        this.category_number = category_number;
    }

    public int getParentCategoryNumber() {
        return parent_category_number;
    }

    public void setParentCategoryNumber(final int parent_category_number) {
        this.parent_category_number = parent_category_number;
    }

    public String getCategoryName() {

        return category_name;
    }

    public void setCategoryName(final String category_name) {

        this.category_name = category_name;

    }


}
