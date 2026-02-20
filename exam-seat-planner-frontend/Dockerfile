# Stage 1 — Build Angular app
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install --registry https://registry.npmjs.org/
COPY . .
RUN npx ng build --configuration production

# Show what was actually built (helps debug path)
RUN find /app/dist -type d

# Stage 2 — Serve with Nginx
FROM nginx:alpine
# Try this path first — covers Angular 17+ with new application builder
COPY --from=build /app/dist/exam-seat-planner-frontend /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]