package sgtmelon.livewallpaper.office.st;

import java.util.Arrays;

public class StMode {

    private boolean active = false;
    private boolean[] select = new boolean[0];
    private int count = 0;

    public StMode() {

    }

    public StMode(int size) {
        active = false;

        select = new boolean[size];
        Arrays.fill(select, false);

        count = 0;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean[] getSelect() {
        return select;
    }

    public boolean getSelect(int position) {
        return select[position];
    }

    public void setSelect(boolean[] select) {
        this.select = select;
    }

    public void setSelect(int position) {
        select[position] = !select[position];

        if (select[position]) count++;
        else count--;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
