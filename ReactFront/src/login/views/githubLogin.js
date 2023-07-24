import React from "react";
import { call } from "../service/ApiService";
import { searchQuery } from "../service/searchQuery";

export function GithubLogin() {
  const access_token = searchQuery("token");
  const member_id = searchQuery("member_id");
  const git_id = searchQuery("git_id");
  // 회원가입 x : false , 회원가입 o : true
  const result = searchQuery("result");
  localStorage.setItem("ACCESS_TOKEN", access_token);
  sessionStorage.setItem("MEMBER_ID", member_id);
  sessionStorage.setItem("GIT_ID", git_id);

  if (result === "1") {
    window.location.href = "/";
  } else {
    window.location.href = "/signup?git_id=" + git_id;
  }
}
