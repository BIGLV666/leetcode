select a.player_id,  MIN(A.event_date) as first_login from Activity as a
group by a.player_id