# Created a App to fetch movies List from remote server and show it on the screen.
 Created a detail page of any movie that is being clicked <br /> 
 Also handled case where data is being saved in roomdatabase and will be fetched in case of failure.<br /> 
 Created a Search bar where you can search specific movie from list.<br /> 
 Created a BookMarkFragment where bookmark elements are being shown.<br /> 




Detail description of the packages that are being created:-<br /> 
   1)UI-> contains information about the activity and fragments that are being used in this project.<br /> 
         MainActivity-> activity where list of movies items are to be shown.<br /> 
         DetailActivity -> activity where detailed information about any particular movie is being shown.<br /> 
         BookMarkFragment -> a bottom sheet fragment where all the bookmark items are being shown. <br /> 
   2) adapter -> all the adapters that are being used on movies listing,detail or bookmark page.<br /> 
   3) base -> base classes that are being used inside project <br /> 
   4) binding -> BindingAdapter implementation is wriiten there (used to set items in recyclerview + used to fetch images through picasso)<br /> 
   5) custom -> verticalItemDecorator is used to give vertical margin b/w the items of recyclerview<br /> 
   6) database -> all infos about the databases are written there which includes entity,dao,rommdatabase info.<br /> 
   7) event -> these are basically string constants used while sending events from viewModel to activity<br /> 
   8) factory -> viewmodelfactory is impemented here.<br /> 
   9) helper -> helper classes implemetation is written here<br /> 
   10) model -> data classes that is being required for fetching response from remote server are written here.<br /> 
   11) netwrok -> network layer implementation is written here.(retrofit)<br /> 
   12) viewModel -> all the viewModels and their implementation are written here.<br /> 
