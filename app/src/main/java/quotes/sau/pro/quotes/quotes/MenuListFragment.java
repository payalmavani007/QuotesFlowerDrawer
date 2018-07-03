
package quotes.sau.pro.quotes.quotes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


/**
 * Created by mxn on 2016/12/13.
 * MenuListFragment
 */

public class MenuListFragment extends Fragment {

    private ImageView ivMenuUserProfilePhoto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        RelativeLayout relativeLayout = view.findViewById(R.id.relative);

        ivMenuUserProfilePhoto =  view.findViewById(R.id.ivMenuUserProfilePhoto);
        NavigationView vNavigation = view.findViewById(R.id.vNavigation);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                Fragment fragment = null;
                if (id == R.id.home)
                {
                    fragment = new HomeFragment();
                }
                else if (id == R.id.categories)
                {
                    fragment = new CategoryFragment();
                }
                else if (id == R.id.author)
                {
                    fragment = new AuthorFragment();
                }
                else if (id == R.id.user_login)
                {
                    fragment = new UserLoginFragment();
                }
                if (fragment != null)
                {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_containt,fragment);
                    fragmentTransaction.commit();
                }

                return true;
            }
        }) ;

        return  view ;
    }



}
