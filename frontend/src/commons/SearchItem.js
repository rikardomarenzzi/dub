import React, { useState,useEffect,useContext } from 'react';
import { useHistory } from 'react-router-dom';
import SearchContext from "../context/SearchContext";


export default function SearchItem({searchItem}){
  const history = useHistory();
  const {getMovieById,getActorById,getVoiceActorById,searchType} = useContext(SearchContext);


  if(searchType === "crew"){
    return(
      <>
        <div>{searchItem.name}</div>
        <div>{searchItem.character}</div>
        <img alt="SearchItemImage" src={searchItem.image} onClick={onImageClick} onError={noImage} height="231px" width="154px" />
      </>
    )
  }

  return(
    <>
      <div>{searchItem.name}</div>
      <img alt="SearchItemImage" src={searchItem.image} onClick={onImageClick} onError={noImage} height="231px" width="154px" />
    </>
  )


  function noImage(event){
    event.target.src = "/imageerror.png";
  }

  function onImageClick() {

    switch (searchItem.type) {

      case "movie":
        getMovieById(searchItem.id).then(() => history.push(`/${searchItem.type}/${searchItem.id}`));
        break;

      case "actor":
        getActorById(searchItem.id).then(() => history.push(`/${searchItem.type}/${searchItem.id}`));
        break;

      case "voiceactor":
        getVoiceActorById(searchItem.id).then(() => history.push(`/${searchItem.type}/${searchItem.id}`));
        break;

      default:
        break;
    }

  }

  function onRefresh() {

    let currentPath = window.location.pathname.split("/")

    /*if (window.performance) {
      console.info("window.performance works fine on this browser");
    }*/

    if (performance.navigation.type === performance.navigation.TYPE_RELOAD) {
      console.info("This page is reloaded");
      const previousId = currentPath[2];
      const previousSearchType = currentPath[1]
      switch (previousSearchType) {

        case "movie":
          getMovieById(previousId);
          break;

        case "Acting":
          getActorById(previousId);
          break;

        case "VoiceActor":
          getVoiceActorById(previousId);
          break;

        default:
          break;
      }
    }

  }


}