import React from "react";
import { Link } from "react-router-dom";
import "./Promotion.css";

const PromotionCard = ({ promotion }) => {
  return (
    <div className="card1">
      <img
        className="card-images"
        src={promotion.promotion_img}
        alt="Promotion Ride"
      />

      <div className="card-body1">
        <h5 className="card-title">{promotion.promotion_content}</h5>
        <p className="card-text">{promotion.promotion_name}</p>
        <button className="btn btn-info text-white" type="button">
          <Link
            to={`/Promotionprice?promotion_id=${promotion.promotion_id}`}
            style={{ textDecoration: "none" }}
          >
            보러 가기
          </Link>
        </button>
      </div>
    </div>
  );
};

export default PromotionCard;
