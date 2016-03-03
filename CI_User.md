

# Introduction #
A user could be an [association](CI_User_Association.md), a [group](CI_User_Group.md),
a [role](CI_User_Role.md), a [person](CI_User_Person.md) (with related business object)
or an [administration person](CI_User_PersonAdmin.md) (without related business
object). Generally for all user also the workspace objects are always handled
(exported and updated).


---


# Parameter Definitions #
| **Name:** `UserIgnoreWSO4Users`         <p><b>Parameters:</b> <code>‑‑ignoreworkspaceobjectsforuser [USER_MATCH]</code>, <code>‑‑ignorewso4user [USER_MATCH]</code></p><p>Defines the match of users for which users workspace objects are not handled (wether exported nor updated). Attention! A workspace object defined in the TCL update file are not ignored and will be created!</p> |
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|