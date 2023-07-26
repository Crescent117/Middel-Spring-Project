package com.exciting.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class OrderDTO {
	private int order_id;
	private Timestamp order_date;
	private int order_amount;
	private boolean check;
}
