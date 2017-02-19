SELECT campaign_id, title, statement, goal, create_user_id, first_name, last_name
FROM campaign
INNER JOIN user ON campaign.create_user_id = user.user_id
WHERE campaign_id=/*campaignId*/1;
