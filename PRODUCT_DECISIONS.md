# Product & Delivery Document

## Problem Interpretation

Merchant onboarding lacks visibility and structured tracking. As a result:

* Teams cannot identify where merchants drop off
* Stalled merchants go unnoticed
* Intervention is inconsistent

This leads to reduced activation rates and inefficiencies.

---

## Focus Area

I chose to focus on **internal operations teams** because:

* They directly influence activation outcomes
* Improving their visibility and tools has immediate impact
* Merchant experience improvements depend on internal execution

---

## Assumptions

* Merchants drop off mid-process due to friction or confusion
* There is no centralized tracking of onboarding progress
* Internal teams act reactively instead of proactively
* Lack of audit trail reduces accountability

---

## MVP Chosen

An **internal activation tracking system** that:

* Tracks onboarding steps
* Computes progress dynamically
* Detects stuck merchants (48h inactivity)
* Enables intervention through notes

---

## Alternatives Considered

### Merchant UI First

Rejected because the root issue is operational visibility, not just UX.

### Notifications (SMS/Email)

Rejected because automation without visibility can be ineffective.

### Analytics Only

Rejected because insights without action are insufficient.

---

## Why This Approach

The solution prioritizes:

* Visibility (what’s happening)
* Diagnosis (where issues occur)
* Action (intervention capability)

---

## Trade-offs

* Prioritized backend over frontend
* Focused on internal users over merchants
* Chose simplicity over configurability

---

## What Was Left Out

* Merchant-facing UI
* Notifications
* Authentication
* Advanced analytics

These can be layered on later.

---

## Delivery Approach

### Build Sequence

1. Data model (merchant, steps, progress)
2. Progress & status computation
3. Stuck detection logic
4. Intervention system (notes)
5. API layer (pagination, filtering)
6. Seed data

---

## Measuring Success

### Metrics

* Activation rate
* Time to activation
* Number of stuck merchants
* Time to intervention

---

## If Results Are Weak

* Identify drop-off steps
* Evaluate internal usage
* Assess effectiveness of interventions

---

## Next Steps (With More Time)

* Add notifications (SMS/email reminders)
* Build lightweight merchant UI
* Add analytics (drop-off per step)
* Introduce SLA tracking
* Enable workflow configurability

---

## Conclusion

The solution focuses on improving visibility, accountability, and intervention — the key drivers of activation success — before introducing automation or UI enhancements.
