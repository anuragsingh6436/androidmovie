# Created a App to fetch movies List from remote server and show it on the screen.
 Created a detail page of any movie that is being clicked \n
 Also handled case where data is being saved in roomdatabase and will be fetched in case of failure.
 Created a Search bar where you can search specific movie from list.
 Created a BookMarkFragment where bookmark elements are being shown.




Detail description of the packages that are being created:-
1)UI-> contains information about the activity and fragments that are being used in this project.
  MainActivity-> activity where list of movies items are to be shown.
  DetailActivity -> activity where detailed information about any particular movie is being shown.
  BookMarkFragment -> a bottom sheet fragment where all the bookmark items are being shown. 
2) adapter -> all the adapters that are being used on movies listing,detail or bookmark page.
3) base -> base classes that are being used inside project
4) binding -> BindingAdapter implementation is wriiten there (used to set items in recyclerview + used to fetch images through picasso)
5) custom -> verticalItemDecorator is used to give vertical margin b/w the items of recyclerview
6) database -> all infos about the databases are written there which includes entity,dao,rommdatabase info.
7) event -> these are basically string constants used while sending events from viewModel to activity
8) factory -> viewmodelfactory is impemented here.
9) helper -> helper classes implemetation is written here
10) model -> data classes that is being required for fetching response from remote server are written here.
11) netwrok -> network layer implementation is written here.(retrofit)
12) viewModel -> all the viewModels and their implementation are written here.
