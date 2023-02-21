package Controller;

import common.User;

import javax.swing.text.html.parser.Entity;

public class EntityUserController {

        private User user = null;
        //Constructors
        public EntityUserController() {}
        public EntityUserController(User givenUser) {user = givenUser;}

        public User getUser() {
            return user;
        }
        public void setUser(User user) {
            this.user = user;
        }

        public boolean isExist() {
            return !(user == null);
        }

    }
