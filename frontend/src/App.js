import React from "react";
import { Route, Switch, Redirect } from 'react-router-dom';
import HomePage from "./homePage/HomePage";
import SearchContextProvider from "./context/SearchContextProvider";
import SearchPage from "./searchPage/SearchPage";
import MovieDetailsPage from "./detailsPage/MovieDetailsPage";
import ActorDetailsPage from "./detailsPage/ActorDetailsPage";
import PreviewPage from "./previewPage/PreviewPage";
import VoiceActorDetailsPage from "./detailsPage/VoiceActorDetailsPage";
import VoiceActorActorListPage from "./voiceActorActorList/VoiceActorActorListPage";

function App() {
  return (
    <SearchContextProvider>
      <Switch>
        <Route path="/home" component={HomePage}/>
        <Route path="/search" component={SearchPage}/>
        <Route path="/movie" component={MovieDetailsPage}/>
        <Route path="/actor" component={ActorDetailsPage}/>
        <Route path="/voiceactor" component={VoiceActorDetailsPage}/>
        <Route path="/voiceactoractorlistpage/:id" component={VoiceActorActorListPage}/>
        <Route path="/previewpage" component={PreviewPage}/>
        <Route path="/">
          <Redirect to="/home"/>
        </Route>
      </Switch>
    </SearchContextProvider>
  );
}

export default App;
